import controller.ToDatawarehouseController;
import mail.EmailService;
import model.Config;
import model.Constant;
import model.Log;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Constant constant = new Constant(); // doc du lieu tu file config
        ToDatawarehouseController toDatawarehouseController = new ToDatawarehouseController();
        EmailService emailService = new EmailService();
        String to = constant.ADMIN_EMAIL;
        String subject = "DataWarehouse project";

        //kiem tra status cua server
        Config config = toDatawarehouseController.checkStatus(constant.TO_STAGING_ACTION, constant.CONTROL_DB);

        if (config != null) { // neu da co log hop ve va co the bat dau thuc hien ghi vao staging
            int re;
            //ghi log toDatawarehouse status Processing
            Log log = new Log(config.id, constant.TO_WAREHOUSE_ACTION, config.createBy, constant.PROCESSING_STATUS);
            re = toDatawarehouseController.insertLog(log, constant.CONTROL_DB);
            if (re == 1) {
                System.out.println("Da ghi log toDatawarehouse processing");
            }

            try {
                re = toDatawarehouseController.runToDatawarehouseProcess(constant.WAREHOUSE_DB);
                if (re == 1) {

                    System.out.println("ghi vao " + constant.WAREHOUSE_DB + " thanh cong");
                    //tao va ghi log khi thanh cong
                    log = new Log(config.id, constant.TO_WAREHOUSE_ACTION, "User", constant.SUCCESS_STATUS);
                    re = toDatawarehouseController.insertLog(log, constant.CONTROL_DB);
                    if (re == 1) System.out.println("Da ghi log processing");
                    //mail khi thanh cong
                    String mess = "ghi vao " + constant.WAREHOUSE_DB + " thanh cong";
                    emailService.send(to, subject, mess);
                } else {

                    System.out.println("ghi vao " + constant.WAREHOUSE_DB + " that bai");
                    //mail khi that bai
                    String mess = "ghi vao " + constant.WAREHOUSE_DB + " that bai";
                    emailService.send(to, subject, mess);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);

            }
        } else {
            System.out.println("Chua the thu hien do chu den lich");
        }
    }
}