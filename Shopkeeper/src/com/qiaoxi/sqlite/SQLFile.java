package com.qiaoxi.sqlite;

/**
 /**
 * Created by shiyan on 2016/3/10.
 */

/*
* �����Ǵ������ݿ��Ľű�*/
public final class SQLFile {
    //TODO
        public static final String hotels="create table Hotels\n" +
            "(\n" +
            "\tid integer not null,\n" +
            "\tname nvarchar(20) not null,\n" +
            "\taddress nvarchar(128) not null,\n" +
            "\ttel nvarchar(20),\n" +
            "\tprimary key(id)\n" +
            ")";
    //TODO
        public static final String areas="create table Areas\n" +
            "(\n" +
            "\tid nvarchar(3) not null,\n" +
            "\tname nvarchar(20) not null,\n" +
            "\tdescription nvarchar(128),\n" +
            "\tusable bit,\n" +
            "\tdepartmentid integer,\n" +
            "\tprimary key(id)\n" +
            ")";
    //TODO
        public static final String desks="CREATE TABLE Desks\n" +
            "(\n" +
            "Id\tnvarchar(4) not null,\n" +
            "Name\tnvarchar(20) not null,\t\t\n" +
            "AreaId\tnvarchar(2)\t ,\n" +
            "_Order\tINTEGER,\t\n" + //ע��˴�_Order
            "Description\tnvarchar(128),\t\t\t\n" +
            "QrCode\tnvarchar(5),\t\t\t\n" +
            "MinPrice\tdecimal(11, 2),\t\t\n" +
            "Status\tinteger\t,\n" +
            "Usable\tbit\t,\n" +
            "primary key (Id)\n" +
            ")";
    //TODO
        public static final String menus="create table Menus\n" +
            "(\n" +
             "\tId\tnvarchar(6)\tnot null,\n" +
            "Code\tnvarchar(10) not null,\n" +
            "Name\tnvarchar(30) not null,\n" +
            "NameAbbr\tnvarchar(15) ,\n" +
            "isfixed\tbit\t ,\n" +
            "Unit\tnvarchar(3)\t,\t\t\n" +
            "Status\tINTEGER ,\t\t\n" +
            "Supplydate\tinteger ,\t\n" +
            "Ordered\tinteger ,\t\t\n" +
            "SourDegree\tinteger,\t\t\n" +
            "SweetDegree\tinteger\t,\t\n" +
            "SatlyDegree\tinteger,\t\t\n" +
            "SpicyDegree\tinteger,\t\t\n" +
            "DepartmentId\tnvarchar(6)\t,\t\t\n" +
            "MinOrderCount\tinteger ,\n" +
            "IsSetMeal\tbit\t,\n" +
            "Usable\tbit\t,\n" +
            "primary key(Id)\n" +
            ")";


        public static final String menuClassMenus="create table MenuClassMenus\n" +
            "(\n" +
            "\tMenu_id nvarchar(6) not null,\n" +
            "\tMenuClass_Id nvarchar(6) not null,\n" +
            "\tprimary key(Menu_id,MenuClass_Id)\n" +
            ")";
        //��Ʒ���
        public static final String menuclasses="create table MenuClasses\t\n" +
                "(\n" +
                "\tId\tnvarchar(6)\tprimary key,\n" +
                "Name\tnvarchar(20)\t,\t\n" +
                "Description\tnvarchar(50)\t,\t\n" +
                "IsShow\tbit\t,\n" +
                "Usable\tbit\t,\t\n" +
                "_Level\tinteger\t,\n" +
                "IsLeaf\tbit\t,\n" +
                "ParentMenuClassId\tnvarchar(6)\n" +
                ")";
            //TODO
        public static final String menuPrices="create table MenuPrices\n" +
                "(\n" +
                "\tId\tnvarchar(6)\tnot null,\t\n" +
                "\tPrice\tdecimal(11, 2) ,\t\t\n" +
                "\tExcludePayDiscount\tbit\t,\t\t\n" +
                "\tDiscount\tfloat ,\n" +
                "\tPoint\tinteger,\n" +
                "\tprimary key(Id)\n" +
                ")";
    //TODO
        public static final String menuRemarks="create table MenuRemarks\n" +
                "(\n" +
                "\tMenu_Id\tnvarchar(6)\tnot null,\t\t\n" +
                "\tRemark_Id\tinteger not null,\n" +
                "\tprimary key(Menu_Id,Remark_Id)\n" +
                ")";
        public static final String menuOnSales="create table MenuOnSales\n" +
                "(\n" +
                "\tId\tnvarchar(6)\tnot null,\t\n" +
                "OnSaleWeek\tinteger,\t\n" +
                "Price\tdecimal(11, 2),\n" +
                "primary key(Id, OnSaleWeek)\t\n" +
                ")";
        public static final String menuSetMeals="create table MenuSetMeals\n" +
                "(\n" +
                "\tMenuId\tnvarchar(6)\tnot null,\t\n" +
                "MenuSetId\tnvarchar(6)\tnot null,\t\n" +
                "_Count\tinteger not null,\n" +
                "primary key(MenuId, MenuSetId)\n" +
                ")";
        public static final String dines="create table Dines\n" +
                "(\n" +
                "\tId\tnvarchar(14)\tnot null,\n" +
                "ClerkID\tnvarchar(8)\t,\t\t\n" +
                "WaiterID\tnvarchar(8),\t\t\n" +
                "UserID\tnvarchar(10),\t\t\n" +
                "HeadCount\tinteger ,\t\t\n" +
                "_Type\tinteger,\t\t\n" +
                "DeskId\tnvarchar(3)\t,\t\t\n" +
                "BeginTime\tdatetime ,\t\t\n" +
                "Status\tinteger,\t\t\n" +
                "OriPrice\tdecimal(11, 2),\t\t\n" +
                "Price\tdecimal(11, 2),\t\t\n" +
                "Discount\tfloat\t,\t\t\n" +
                "Name\tnvarchar(25),\t\t\n" +
                "Invoice\tnvarchar(20),\t\t\n" +
                "IsInvoiced\tbit\t,\t\t\n" +
                "Footer\tnvarchar(15),\t\t\n" +
                "IsRefund\tbit\t,\t\t\n" +
                "IsOnline\tbit\t,\n" +
                "IsPaid\tbit\t,\n" +
                "ReturnedWaiterId\tnvarchar(8)\t,\n" +
                "primary key(Id)\n" +
                ")";

        public static final String dinePaidDetail="\n" +
                "create table DinePaidDetail\n" +
                "(\n" +
                "DineId\tnvarchar(14),\n" +
                "PayKindId\tint\t,\n" +
                "Price\tdecimal(11, 2)\n," +
                "primary key(dineid, paykindid)" +
                ")";
        //������ע
        public static final String dineRemarks="create table DineRemarks\n" +
                "(\n" +
                "\tDineId\tnvarchar(14)\tnot null,\t\n" +
                "Remark_Id\tinteger not null,\n" +
                "primary key(DineId,Remark_Id)\n" +
                ")";
        //������Ĳ�Ʒ
        public static final String dineMenus="create table DineMenus\n" +
                "(\n" +
//                "_id integer primary key autoincrement\n"+
                "\tDineId\tnvarchar(14) not null,\t\n" +
                "MenuId\tnvarchar(6) not null,\t\t\n" +
                "_Count\tinteger\t,\n" +
                "Price\tdecimal(11, 2),\t\t\n" +
                "OriPrice\tdecimal(11, 2),\t\t\t\n" +
                "Status\tinteger,\n" +
                "ReturnedWaiterId\tnvarchar(256),\n" +
                "\tprimary key(DineId, MenuId)\n" +
                ")";
        //����ÿ���˵ı�ע
        public static final String dineMenuRemarks="create table DineMenuRemarks\n" +
                "(\n" +
                "\tDineMenu_DineId\tnvarchar(14)\tnot null,\t\n" +
                "DineMenu_MenuId\tnvarchar(6)\tnot null,\n" +
                "DineMenu_Status\tinteger not null,\t\n" +
                "Remark_Id\tinteger not null,\n" +
                "primary key(DineMenu_DineId, DineMenu_MenuId)\n" +
                ")";
        public static final String payKinds="create table PayKinds\n" +
                "(\n" +
                "\tId\tinteger primary key AUTOINCREMENT,\n" +
                "Name\tnvarchar(20) not null,\n" +
                "_Type\tinteger,\n" +
                "Description\tnvarchar(128),\t\t\t\n" +
                "Discount\tfloat,\n" +
                "Usable\tbit\t,\n" +
                "IsOnline\tbit,\t\n" +
                "IsShow\tbit\t\t,\t\n" +
                "RedirectUrl\tnvarchar(128),\t\t\t\n" +
                "CompleteUrl\tnvarchar(128),\t\n" +
                "NotifyUrl\tnvarchar(128)\n" +
                ")";


        public static final String remarks= "create table Remarks\n" +
                "(\n" +
                "\t\t\n" +
                "Id\tint\tprimary key ,\n" +
                "Name\tnvarchar(10),\t\n" +
                "Price\tdecimal(11, 2)\n" +
                ")";


        public static final String staff="create table Staffs\n" +
                "(\n" +
                "Id\tnvarchar(8)\tnot null,\n" +
                "Name\tnvarchar(10)\tnot null,\t\t\n" +
                "DineCount\tinteger,\n" +
                "DinePrice\tdecimal(11, 2),\t\n" +
                "WorkTimeFrom\ttime(7)\t,\n" +
                "WorkTimeTo\ttime(7)\t,\n" +
                "primary key(Id)\n" +
                ")";

        public static final String staffRoles="create table StaffRoles\n" +
                "(\n" +
                "\tId\tinteger\tprimary key AUTOINCREMENT,\t\n" +
                "Name\tnvarchar(5)\tnot null\n" +
                ")";

        public static final String staffStaffRoles="create table StaffStaffRoles\n" +
                "(\n" +
                "\tStaff_Id\tnvarchar(8)\tnot null,\t\t\n" +
                "StaffRole_Id\tinteger not null,\n" +
                "primary key(Staff_Id,StaffRole_Id)\n" +
                ")";

        public static final String users="create table Users\n" +
                "(\n" +
                "Id\tnvarchar(10)\tnot null,\t\n" +
                "PhoneNumber\tnvarchar(11),\t\t\n" +
                "Email\tnvarchar(128),\t\t\n" +
                "UserName\tnvarchar(20),\n" +
                "PasswordHash\tnvarchar(256),\t\t\n" +
                "Confirmed\tbit\t,\n" +
                "IsSendRecommedation\tbit\t,\t\n" +
                "CreateDate\tdatetime,\n" +
                "primary key(Id)\t\n" +
                ")";


        public static final String CONTRACT_PATTERN =
                "package %s;\n" + //����
                "import android.provider.BaseColumns;\n" +
                "public final class DBManagerContract {\n" +
                "\n" +
                "    public DBManagerContract(){}\n" +
                    "%s" +//������Ҫ����
                "}";

        public static final String CONTRACT_PATTERN_1 =
                "\tpublic static abstract class %sTable implements BaseColumns{\n" +
                "\t\tpublic static final String TABLE_NAME = \"%s\";\n" +
                "%s\n" +
                "\t}\n";
        public static final String CONTRACT_PATTERN_2 =
                "\t\tpublic static final String COLUMN_NAME_%s = \"%s\";\n";

}
