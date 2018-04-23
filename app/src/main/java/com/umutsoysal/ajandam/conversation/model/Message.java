package com.umutsoysal.ajandam.conversation.model;


public class Message
{

    String mesajText;
    String gonderici;
    String gondericiPP;
    String zaman;
    String uri;

    public Message()
    {
    }

    public Message(String mesajText, String gonderici,String gondericiPP, String zaman, String uri)
    {
        this.mesajText = mesajText;
        this.gonderici = gonderici;
        this.zaman = zaman;
        this.uri = uri;
        this.gondericiPP=gondericiPP;

    }
    public String getGondericiPP()
    {
        return gondericiPP;
    }

    public void setGondericiPP(String gondericiPP)
    {
        this.gondericiPP = gondericiPP;
    }

    public String getMesajText()
    {
        return mesajText;
    }

    public void setMesajText(String mesajText)
    {
        this.mesajText = mesajText;
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getGonderici()
    {
        return gonderici;
    }

    public void setGonderici(String gonderici)
    {
        this.gonderici = gonderici;
    }

    public String getZaman()
    {
        return zaman;
    }

    public void setZaman(String zaman)
    {
        this.zaman = zaman;
    }


}