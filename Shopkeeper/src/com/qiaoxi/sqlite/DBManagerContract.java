package com.qiaoxi.sqlite;

import android.provider.BaseColumns;
public final class DBManagerContract {

    public DBManagerContract(){}
    public static abstract class AreasTable implements BaseColumns{
        public static final String TABLE_NAME = "Areas";
        public static final String COLUMN_NAME_id = "id";
        public static final String COLUMN_NAME_name = "name";
        public static final String COLUMN_NAME_description = "description";
        public static final String COLUMN_NAME_usable = "usable";
        public static final String COLUMN_NAME_departmentid = "departmentid";

    }
    public static abstract class DesksTable implements BaseColumns{
        public static final String TABLE_NAME = "Desks";
        public static final String COLUMN_NAME_Id = "Id";
        public static final String COLUMN_NAME_Name = "Name";
        public static final String COLUMN_NAME_AreaId = "AreaId";
        public static final String COLUMN_NAME__Order = "_Order";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_QrCode = "QrCode";
        public static final String COLUMN_NAME_MinPrice = "MinPrice";
        public static final String COLUMN_NAME_Status = "Status";
        public static final String COLUMN_NAME_Usable = "Usable";

    }
    public static abstract class DineMenuRemarksTable implements BaseColumns{
        public static final String TABLE_NAME = "DineMenuRemarks";
        public static final String COLUMN_NAME_DineMenu_DineId = "DineMenu_DineId";
        public static final String COLUMN_NAME_DineMenu_MenuId = "DineMenu_MenuId";
        public static final String COLUMN_NAME_DineMenu_Status = "DineMenu_Status";
        public static final String COLUMN_NAME_Remark_Id = "Remark_Id";

    }
    public static abstract class DineMenusTable implements BaseColumns{
        public static final String TABLE_NAME = "DineMenus";
        public static final String COLUMN_NAME_DineId = "DineId";
        public static final String COLUMN_NAME_MenuId = "MenuId";
        public static final String COLUMN_NAME__Count = "_Count";
        public static final String COLUMN_NAME_Price = "Price";
        public static final String COLUMN_NAME_OriPrice = "OriPrice";
        public static final String COLUMN_NAME_Status = "Status";
        public static final String COLUMN_NAME_ReturnedWaiterId = "ReturnedWaiterId";

    }
    public static abstract class DinePaidDetailTable implements BaseColumns{
        public static final String TABLE_NAME = "DinePaidDetail";
        public static final String COLUMN_NAME_DineId = "DineId";
        public static final String COLUMN_NAME_PayKindId = "PayKindId";
        public static final String COLUMN_NAME_Price = "Price";

    }
    public static abstract class DineRemarksTable implements BaseColumns{
        public static final String TABLE_NAME = "DineRemarks";
        public static final String COLUMN_NAME_DineId = "DineId";
        public static final String COLUMN_NAME_Remark_Id = "Remark_Id";

    }
    public static abstract class DinesTable implements BaseColumns{
        public static final String TABLE_NAME = "Dines";
        public static final String COLUMN_NAME_Id = "Id";
        public static final String COLUMN_NAME_ClerkID = "ClerkID";
        public static final String COLUMN_NAME_WaiterID = "WaiterID";
        public static final String COLUMN_NAME_UserID = "UserID";
        public static final String COLUMN_NAME_HeadCount = "HeadCount";
        public static final String COLUMN_NAME__Type = "_Type";
        public static final String COLUMN_NAME_DeskId = "DeskId";
        public static final String COLUMN_NAME_BeginTime = "BeginTime";
        public static final String COLUMN_NAME_Status = "Status";
        public static final String COLUMN_NAME_OriPrice = "OriPrice";
        public static final String COLUMN_NAME_Price = "Price";
        public static final String COLUMN_NAME_Discount = "Discount";
        public static final String COLUMN_NAME_Name = "Name";
        public static final String COLUMN_NAME_Invoice = "Invoice";
        public static final String COLUMN_NAME_IsInvoiced = "IsInvoiced";
        public static final String COLUMN_NAME_Footer = "Footer";
        public static final String COLUMN_NAME_IsRefund = "IsRefund";
        public static final String COLUMN_NAME_IsOnline = "IsOnline";
        public static final String COLUMN_NAME_IsPaid = "IsPaid";
        public static final String COLUMN_NAME_ReturnedWaiterId = "ReturnedWaiterId";

    }
    public static abstract class HotelsTable implements BaseColumns{
        public static final String TABLE_NAME = "Hotels";
        public static final String COLUMN_NAME_id = "id";
        public static final String COLUMN_NAME_name = "name";
        public static final String COLUMN_NAME_address = "address";
        public static final String COLUMN_NAME_tel = "tel";

    }
    public static abstract class MenuClassMenusTable implements BaseColumns{
        public static final String TABLE_NAME = "MenuClassMenus";
        public static final String COLUMN_NAME_Menu_id = "Menu_id";
        public static final String COLUMN_NAME_MenuClass_Id = "MenuClass_Id";

    }
    public static abstract class MenuClassesTable implements BaseColumns{
        public static final String TABLE_NAME = "MenuClasses";
        public static final String COLUMN_NAME_Id = "Id";
        public static final String COLUMN_NAME_Name = "Name";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_IsShow = "IsShow";
        public static final String COLUMN_NAME_Usable = "Usable";
        public static final String COLUMN_NAME__Level = "_Level";
        public static final String COLUMN_NAME_IsLeaf = "IsLeaf";
        public static final String COLUMN_NAME_ParentMenuClassId = "ParentMenuClassId";

    }
    public static abstract class MenuOnSalesTable implements BaseColumns{
        public static final String TABLE_NAME = "MenuOnSales";
        public static final String COLUMN_NAME_Id = "Id";
        public static final String COLUMN_NAME_OnSaleWeek = "OnSaleWeek";
        public static final String COLUMN_NAME_Price = "Price";

    }
    public static abstract class MenuPricesTable implements BaseColumns{
        public static final String TABLE_NAME = "MenuPrices";
        public static final String COLUMN_NAME_Id = "Id";
        public static final String COLUMN_NAME_Price = "Price";
        public static final String COLUMN_NAME_ExcludePayDiscount = "ExcludePayDiscount";
        public static final String COLUMN_NAME_Discount = "Discount";
        public static final String COLUMN_NAME_Point = "Point";

    }
    public static abstract class MenuRemarksTable implements BaseColumns{
        public static final String TABLE_NAME = "MenuRemarks";
        public static final String COLUMN_NAME_Menu_Id = "Menu_Id";
        public static final String COLUMN_NAME_Remark_Id = "Remark_Id";

    }
    public static abstract class MenuSetMealsTable implements BaseColumns{
        public static final String TABLE_NAME = "MenuSetMeals";
        public static final String COLUMN_NAME_MenuId = "MenuId";
        public static final String COLUMN_NAME_MenuSetId = "MenuSetId";
        public static final String COLUMN_NAME__Count = "_Count";

    }
    public static abstract class MenusTable implements BaseColumns{
        public static final String TABLE_NAME = "Menus";
        public static final String COLUMN_NAME_Id = "Id";
        public static final String COLUMN_NAME_Code = "Code";
        public static final String COLUMN_NAME_Name = "Name";
        public static final String COLUMN_NAME_NameAbbr = "NameAbbr";
        public static final String COLUMN_NAME_isfixed = "isfixed";
        public static final String COLUMN_NAME_Unit = "Unit";
        public static final String COLUMN_NAME_Status = "Status";
        public static final String COLUMN_NAME_Supplydate = "Supplydate";
        public static final String COLUMN_NAME_Ordered = "Ordered";
        public static final String COLUMN_NAME_SourDegree = "SourDegree";
        public static final String COLUMN_NAME_SweetDegree = "SweetDegree";
        public static final String COLUMN_NAME_SatlyDegree = "SatlyDegree";
        public static final String COLUMN_NAME_SpicyDegree = "SpicyDegree";
        public static final String COLUMN_NAME_DepartmentId = "DepartmentId";
        public static final String COLUMN_NAME_MinOrderCount = "MinOrderCount";
        public static final String COLUMN_NAME_IsSetMeal = "IsSetMeal";
        public static final String COLUMN_NAME_Usable = "Usable";

    }
    public static abstract class PayKindsTable implements BaseColumns{
        public static final String TABLE_NAME = "PayKinds";
        public static final String COLUMN_NAME_Id = "Id";
        public static final String COLUMN_NAME_Name = "Name";
        public static final String COLUMN_NAME__Type = "_Type";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_Discount = "Discount";
        public static final String COLUMN_NAME_Usable = "Usable";
        public static final String COLUMN_NAME_IsOnline = "IsOnline";
        public static final String COLUMN_NAME_IsShow = "IsShow";
        public static final String COLUMN_NAME_RedirectUrl = "RedirectUrl";
        public static final String COLUMN_NAME_CompleteUrl = "CompleteUrl";
        public static final String COLUMN_NAME_NotifyUrl = "NotifyUrl";

    }
    public static abstract class RemarksTable implements BaseColumns{
        public static final String TABLE_NAME = "Remarks";
        public static final String COLUMN_NAME_Id = "Id";
        public static final String COLUMN_NAME_Name = "Name";
        public static final String COLUMN_NAME_Price = "Price";

    }
    public static abstract class StaffRolesTable implements BaseColumns{
        public static final String TABLE_NAME = "StaffRoles";
        public static final String COLUMN_NAME_Id = "Id";
        public static final String COLUMN_NAME_Name = "Name";

    }
    public static abstract class StaffStaffRolesTable implements BaseColumns{
        public static final String TABLE_NAME = "StaffStaffRoles";
        public static final String COLUMN_NAME_Staff_Id = "Staff_Id";
        public static final String COLUMN_NAME_StaffRole_Id = "StaffRole_Id";

    }
    public static abstract class StaffsTable implements BaseColumns{
        public static final String TABLE_NAME = "Staffs";
        public static final String COLUMN_NAME_Id = "Id";
        public static final String COLUMN_NAME_Name = "Name";
        public static final String COLUMN_NAME_DineCount = "DineCount";
        public static final String COLUMN_NAME_DinePrice = "DinePrice";
        public static final String COLUMN_NAME_WorkTimeFrom = "WorkTimeFrom";
        public static final String COLUMN_NAME_WorkTimeTo = "WorkTimeTo";

    }
    public static abstract class UsersTable implements BaseColumns{
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_NAME_Id = "Id";
        public static final String COLUMN_NAME_PhoneNumber = "PhoneNumber";
        public static final String COLUMN_NAME_Email = "Email";
        public static final String COLUMN_NAME_UserName = "UserName";
        public static final String COLUMN_NAME_PasswordHash = "PasswordHash";
        public static final String COLUMN_NAME_Confirmed = "Confirmed";
        public static final String COLUMN_NAME_IsSendRecommedation = "IsSendRecommedation";
        public static final String COLUMN_NAME_CreateDate = "CreateDate";

    }
    public static abstract class android_metadataTable implements BaseColumns{
        public static final String TABLE_NAME = "android_metadata";
        public static final String COLUMN_NAME_locale = "locale";

    }
    public static abstract class sqlite_sequenceTable implements BaseColumns{
        public static final String TABLE_NAME = "sqlite_sequence";
        public static final String COLUMN_NAME_name = "name";
        public static final String COLUMN_NAME_seq = "seq";

    }
}