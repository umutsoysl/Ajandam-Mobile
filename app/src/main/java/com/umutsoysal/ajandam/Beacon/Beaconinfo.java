package com.umutsoysal.ajandam.Beacon;

public class Beaconinfo
{

    private String beaconName;
    private String beaconUUID;
    private String deviceMac;
    private String beaconMajor;
    private String beaconMinor;

    public Beaconinfo()
    {

    }

    public  Beaconinfo (String BeaconName,String BeaconUid,String DeviceMac,String major ,String minor)
    {
        this.beaconName=BeaconName;
        this.beaconUUID=BeaconUid;
        this.deviceMac=DeviceMac;
        this.beaconMajor=major;
        this.beaconMinor=minor;
    }

    public String getBeaconName()
    {
        return this.beaconName;
    }
    public void setBeaconName(String name)
    {
        this.beaconName=name;
    }

    public String getBeaconUUID()
    {
        return this.beaconUUID;
    }
    public void setBeaconUUID(String uuid)
    {
        this.beaconUUID=uuid;
    }

    public String getDeviceMac()
    {
        return this.deviceMac;
    }
    public void setDeviceMac(String deviceMac)
    {
        this.deviceMac=deviceMac;
    }

    public String getBeaconMajor()
    {
        return this.beaconMajor;
    }
    public void setBeaconMajor(String major)
    {
        this.beaconMajor=major;
    }

    public String getBeaconMinor()
    {
        return this.beaconMinor;
    }
    public void setBeaconMinor(String minor)
    {
        this.beaconMinor=minor;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Beaconinfo that = (Beaconinfo) o;

        return beaconUUID != null ? beaconUUID.equals(that.beaconUUID) : that.beaconUUID == null;
    }

    @Override
    public int hashCode()
    {
        int result = beaconName != null ? beaconName.hashCode() : 0;
        result = 31 * result + (beaconUUID != null ? beaconUUID.hashCode() : 0);
        result = 31 * result + (deviceMac != null ? deviceMac.hashCode() : 0);
        result = 31 * result + (beaconMajor != null ? beaconMajor.hashCode() : 0);
        result = 31 * result + (beaconMinor != null ? beaconMinor.hashCode() : 0);
        return result;
    }
}
