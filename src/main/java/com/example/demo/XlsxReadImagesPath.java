package com.example.demo;

import com.example.demo.dao.PlayerDao;
import com.example.demo.dao.TeamDao;
import com.example.demo.model.Player;
import com.example.demo.model.Team;
import com.example.demo.model.XlsxObject;
import com.example.demo.model.XslxImage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.cli.Command;
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

@SuppressWarnings("Duplicates")
@Component
@Order(2)
public class XlsxReadImagesPath implements CommandLineRunner {
    @Autowired
    private TeamDao teamDao;

    @Autowired
    private PlayerDao playerDao;

    private String teamName;
    private String pathImage;

    public void readXLSXFile() throws IOException {
        InputStream ExcelFileToRead = new FileInputStream("src/main/resources/pathImagen.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

        //XSSFWorkbook test = new XSSFWorkbook();

        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        //XSSFCell cell;

        Iterator rows = sheet.rowIterator();
        List<XslxImage> objects = new ArrayList<>();
        while (rows.hasNext()) {
            row = (XSSFRow) rows.next();
            XslxImage object = new XslxImage();
            object.setTeamName(row.getCell(0).getStringCellValue());
            object.setImgPath(row.getCell(1).getStringCellValue());
            objects.add(object);
            System.out.println(object.toString());
        }

        for (XslxImage object : objects
        ) {
            this.teamName = object.getTeamName();
            this.pathImage = object.getImgPath();
            Team team = teamDao.findByName(this.teamName);
            if (team != null) {
                team.setImgPath(this.pathImage);
                teamDao.save(team);
            }
            else{
                System.out.println("--------------------Team null");
            }
        }

    }

    @Override
    public void run(String... args) throws Exception {
        this.readXLSXFile();
    }
}
