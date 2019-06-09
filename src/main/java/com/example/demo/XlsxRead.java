package com.example.demo;

import com.example.demo.dao.PlayerDao;
import com.example.demo.dao.TeamDao;
import com.example.demo.model.KeyWord;
import com.example.demo.model.Player;
import com.example.demo.model.Team;
import com.example.demo.model.XlsxObject;
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

@Component
@Order(1)
public class XlsxRead implements CommandLineRunner {
    @Autowired
    private TeamDao teamDao;

    @Autowired
    private PlayerDao playerDao;

    private String teamName;
    private String playerName;

    public void readXLSXFile() throws IOException
    {
        InputStream ExcelFileToRead = new FileInputStream("src/main/resources/equiposjugadores.xlsx");
        XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);

        //XSSFWorkbook test = new XSSFWorkbook();

        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        //XSSFCell cell;

        Iterator rows = sheet.rowIterator();
        rows.next();
        List<XlsxObject> objects = new ArrayList<>();
        while (rows.hasNext())
        {
            row=(XSSFRow) rows.next();
            XlsxObject object = new XlsxObject();
            object.setTeamName(row.getCell(0).getStringCellValue());
            object.setPlayerFullName(row.getCell(1).getStringCellValue());
            objects.add(object);
            System.out.println(object.toString());
        }

        for (XlsxObject object: objects
             ) {
            this.teamName = object.getTeamName();
            this.playerName = object.getPlayerFullName();
            if(teamDao.findByName(this.teamName) == null){
                System.out.println("Hola mundo");
                Team team = new Team();
                team.setName(teamName);
                teamDao.save(team);
            }
            if(playerDao.findPlayerByFullName(this.playerName) == null){
                Player player = new Player();
                player.setFullName(playerName);
                Team team = teamDao.findByName(teamName);
                player.setTeam(team);
                playerDao.save(player);
            }
        }

    }

    /*public void readXLSXFileKeyWord() throws IOException
    {
        InputStream ExcelFileToRead2 = new FileInputStream("src/main/resources/keywords.xlsx");
        XSSFWorkbook  wb2 = new XSSFWorkbook(ExcelFileToRead2);

        //XSSFWorkbook test = new XSSFWorkbook();

        XSSFSheet sheet = wb2.getSheetAt(0);
        XSSFRow row2;
        //XSSFCell cell;

        Iterator rows2 = sheet.rowIterator();
        rows2.next();
        List<KeyWord> objects = new ArrayList<>();
        while (rows2.hasNext())
        {
            row2=(XSSFRow) rows2.next();
            KeyWord keyword = new KeyWord();
            keyword.setType(row2.getCell(0).getStringCellValue());
            keyword.setName(row2.getCell(1).getStringCellValue());
            keyword.setKeyWord(row2.getCell(2).getStringCellValue());
            objects.add(keyword);
            System.out.println(keyword.toString());
        }


    }*/
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
