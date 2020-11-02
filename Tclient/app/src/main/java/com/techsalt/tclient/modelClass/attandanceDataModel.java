package com.techsalt.tclient.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class attandanceDataModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("employeeId")
        @Expose
        private Integer employeeId;
        @SerializedName("empcode")
        @Expose
        private String empcode;
        @SerializedName("level")
        @Expose
        private String level;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("checkInTime")
        @Expose
        private String checkInTime;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("checkOutTime")
        @Expose
        private String checkOutTime;
        @SerializedName("checkInAddress")
        @Expose
        private String checkInAddress;
        @SerializedName("checkInLatitude")
        @Expose
        private Double checkInLatitude;
        @SerializedName("checkInLongitude")
        @Expose
        private Double checkInLongitude;
        @SerializedName("checkOutAddress")
        @Expose
        private String checkOutAddress;
        @SerializedName("checkOutLatitude")
        @Expose
        private Double checkOutLatitude;
        @SerializedName("checkOutLongitude")
        @Expose
        private Double checkOutLongitude;
        @SerializedName("startImageName")
        @Expose
        private String startImageName;
        @SerializedName("dutyHours")
        @Expose
        private String dutyHours;
        @SerializedName("checkInBatteryLevel")
        @Expose
        private String checkInBatteryLevel;
        @SerializedName("checkOutBatteryLevel")
        @Expose
        private String checkOutBatteryLevel;
        @SerializedName("qrCodeScanVisitList")
        @Expose
        private List<QrCodeScanVisitList> qrCodeScanVisitList = null;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public Integer getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Integer employeeId) {
            this.employeeId = employeeId;
        }

        public String getEmpcode() {
            return empcode;
        }

        public void setEmpcode(String empcode) {
            this.empcode = empcode;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getCheckInTime() {
            return checkInTime;
        }

        public void setCheckInTime(String checkInTime) {
            this.checkInTime = checkInTime;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCheckOutTime() {
            return checkOutTime;
        }

        public void setCheckOutTime(String checkOutTime) {
            this.checkOutTime = checkOutTime;
        }

        public String getCheckInAddress() {
            return checkInAddress;
        }

        public void setCheckInAddress(String checkInAddress) {
            this.checkInAddress = checkInAddress;
        }

        public Double getCheckInLatitude() {
            return checkInLatitude;
        }

        public void setCheckInLatitude(Double checkInLatitude) {
            this.checkInLatitude = checkInLatitude;
        }

        public Double getCheckInLongitude() {
            return checkInLongitude;
        }

        public void setCheckInLongitude(Double checkInLongitude) {
            this.checkInLongitude = checkInLongitude;
        }

        public String getCheckOutAddress() {
            return checkOutAddress;
        }

        public void setCheckOutAddress(String checkOutAddress) {
            this.checkOutAddress = checkOutAddress;
        }

        public Double getCheckOutLatitude() {
            return checkOutLatitude;
        }

        public void setCheckOutLatitude(Double checkOutLatitude) {
            this.checkOutLatitude = checkOutLatitude;
        }

        public Double getCheckOutLongitude() {
            return checkOutLongitude;
        }

        public void setCheckOutLongitude(Double checkOutLongitude) {
            this.checkOutLongitude = checkOutLongitude;
        }

        public String getStartImageName() {
            return startImageName;
        }

        public void setStartImageName(String startImageName) {
            this.startImageName = startImageName;
        }

        public String getDutyHours() {
            return dutyHours;
        }

        public void setDutyHours(String dutyHours) {
            this.dutyHours = dutyHours;
        }

        public String getCheckInBatteryLevel() {
            return checkInBatteryLevel;
        }

        public void setCheckInBatteryLevel(String checkInBatteryLevel) {
            this.checkInBatteryLevel = checkInBatteryLevel;
        }

        public String getCheckOutBatteryLevel() {
            return checkOutBatteryLevel;
        }

        public void setCheckOutBatteryLevel(String checkOutBatteryLevel) {
            this.checkOutBatteryLevel = checkOutBatteryLevel;
        }

        public List<QrCodeScanVisitList> getQrCodeScanVisitList() {
            return qrCodeScanVisitList;
        }

        public void setQrCodeScanVisitList(List<QrCodeScanVisitList> qrCodeScanVisitList) {
            this.qrCodeScanVisitList = qrCodeScanVisitList;
        }

        public class QrCodeScanVisitList {

            @SerializedName("vpmuid")
            @Expose
            private Integer vpmuid;
            @SerializedName("qrType")
            @Expose
            private String qrType;
            @SerializedName("qrName")
            @Expose
            private String qrName;
            @SerializedName("scanSelfie")
            @Expose
            private String scanSelfie;
            @SerializedName("timeStamp")
            @Expose
            private String timeStamp;

            public Integer getVpmuid() {
                return vpmuid;
            }

            public void setVpmuid(Integer vpmuid) {
                this.vpmuid = vpmuid;
            }

            public String getQrType() {
                return qrType;
            }

            public void setQrType(String qrType) {
                this.qrType = qrType;
            }

            public String getQrName() {
                return qrName;
            }

            public void setQrName(String qrName) {
                this.qrName = qrName;
            }

            public String getScanSelfie() {
                return scanSelfie;
            }

            public void setScanSelfie(String scanSelfie) {
                this.scanSelfie = scanSelfie;
            }

            public String getTimeStamp() {
                return timeStamp;
            }

            public void setTimeStamp(String timeStamp) {
                this.timeStamp = timeStamp;
            }

        }

    }
}
