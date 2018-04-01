package com.soa_arah;

/**
 * Created by kholoud on 3/27/2018 AD.
 */

public class Review {

    private String FName;
    private String RKey;
    private String comment;
    private String numDisLike;
    private String numLike;
    private String writer;



    public Review() {

    }

    public Review(String FName, String RKey, String comment, String numDisLike, String numLike, String writer) {
        this.FName = FName;
        this.RKey = RKey;
        this.comment = comment;
        this.numDisLike = numDisLike;
        this.numLike = numLike;
        this.writer = writer;
    }

    public String getFName() {

        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getRKey() {
        return RKey;
    }

    public void setRKey(String RKey) {
        this.RKey = RKey;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNumDisLike() {
        return numDisLike;
    }

    public void setNumDisLike(String numDisLike) {
        this.numDisLike = numDisLike;
    }

    public String getNumLike() {
        return numLike;
    }

    public void setNumLike(String numLike) {
        this.numLike = numLike;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}
