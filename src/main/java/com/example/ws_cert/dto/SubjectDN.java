package com.example.ws_cert.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubjectDN {

    private String cn;
    private String o;
    private String ou;
    private String l;
    private String st;
    private String c;

//    public void setC(String c) {
//        if (c == null || !c.matches("^[a-zA-Z]{2}$")) {
//            throw new IllegalArgumentException("Mã quốc gia (C) phải gồm đúng 2 chữ cái.");
//        }
//        this.c = c;
//    }

    @Override
    public String toString() {
        String subjectDN = "";
        if (cn != null) {
            subjectDN += "CN=" + cn + ",";
        }
        if (o != null) {
            subjectDN += "O=" + o + ",";
        }
        if (ou != null) {
            subjectDN += "OU=" + ou + ",";
        }
        if (l != null) {
            subjectDN += "L=" + l + ",";
        }
        if (st != null) {
            subjectDN += "ST=" + st + ",";
        }
        if (c != null) {
            subjectDN += "C=" + c + ",";
        }
        if (subjectDN.endsWith(",")) {
            subjectDN = subjectDN.substring(0, subjectDN.length() - 1);
        }
        return subjectDN;
    }
}
