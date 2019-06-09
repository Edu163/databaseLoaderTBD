package com.example.demo;

import com.example.demo.dao.PlayerDao;
import com.example.demo.dao.PlayerNicknameDao;
import com.example.demo.dao.TeamDao;
import com.example.demo.dao.TeamNicknameDao;
import com.example.demo.model.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//@Component
//@Order(2)
public class XlsxReadKeyWords implements CommandLineRunner {
    @Autowired
    private TeamDao teamDao;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private PlayerNicknameDao playerNicknameDao;

    @Autowired
    private TeamNicknameDao teamNicknameDao;

    private String teamName;
    private String playerName;
    private String type;
    private String apodo;
    private String name;

    public void readXLSXFile() throws IOException
    {
        InputStream ExcelFileToRead = new FileInputStream("src/main/resources/keywords.xlsx");
        XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;

        Iterator rows = sheet.rowIterator();
        rows.next();
        List<KeyWord> keywords = new ArrayList<>();
        while (rows.hasNext())
        {
            row=(XSSFRow) rows.next();
            KeyWord keyword = new KeyWord();
            try {
                String type = row.getCell(0).getStringCellValue();
                keyword.setType(type);
            }catch (NullPointerException e){
                System.out.println(e);
            }
            try {
                String name = row.getCell(1).getStringCellValue();
                keyword.setName(name);
            }catch (NullPointerException e){
                System.out.println(e);
            }
            try {
                String word = row.getCell(2).getStringCellValue();
                keyword.setKeyWord(word);
            }catch (NullPointerException e){
                System.out.println(e);
            }
            if(keyword.getName() != null && keyword.getType() != null && keyword.getKeyWord() != null){
                keywords.add(keyword);
            }
            //System.out.println(keyword.toString());
        }
        for (KeyWord keyword: keywords
             ) {
            this.type = keyword.getType();
            this.name = keyword.getName();
            this.apodo = keyword.getKeyWord();
            if(this.type.equals("equipo")){
                TeamNickname teamNickname = new TeamNickname();
                Team team = teamDao.findByName(this.name);
                teamNickname.setNickname(this.apodo);
                teamNickname.setTeam(team);
                teamNicknameDao.save(teamNickname);
            }
            if(this.type.equals("jugador")){
                PlayerNickname playerNickname = new PlayerNickname();
                Player player = playerDao.findPlayerByFullName(this.name);
                playerNickname.setNickname(this.apodo);
                playerNickname.setPlayer(player);
                playerNicknameDao.save(playerNickname);
            }
            System.out.println(keyword.toString());
        }

    }

    @Override
    public void run(String... args) throws Exception {
        this.readXLSXFile();
    }
}
/*while (cells.hasNext())
            {
                cell=(XSSFCell) cells.next();

                if (cell.getCellType() == CellType.STRING)
                {
                    System.out.print(cell.getStringCellValue()+" ");
                }
                else if(cell.getCellType() == CellType.NUMERIC)
                {
                    System.out.print(cell.getNumericCellValue()+" ");
                }
                else
                {
                    //U Can Handel Boolean, Formula, Errors
                }*/
