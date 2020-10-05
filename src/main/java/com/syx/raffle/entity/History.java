package com.syx.raffle.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class History {
    private Integer id;
    private Date date;
    private Integer term;
    private Integer firstNum;
    private Integer secondNum;
    private Integer thirdNum;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Basic
    @Column(name = "term")
    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    @Basic
    @Column(name = "first_num")
    public Integer getFirstNum() {
        return firstNum;
    }

    public void setFirstNum(Integer firstNum) {
        this.firstNum = firstNum;
    }

    @Basic
    @Column(name = "second_num")
    public Integer getSecondNum() {
        return secondNum;
    }

    public void setSecondNum(Integer secondNum) {
        this.secondNum = secondNum;
    }

    @Basic
    @Column(name = "third_num")
    public Integer getThirdNum() {
        return thirdNum;
    }

    public void setThirdNum(Integer thirdNum) {
        this.thirdNum = thirdNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return Objects.equals(id, history.id) &&
                Objects.equals(date, history.date) &&
                Objects.equals(term, history.term) &&
                Objects.equals(firstNum, history.firstNum) &&
                Objects.equals(secondNum, history.secondNum) &&
                Objects.equals(thirdNum, history.thirdNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, term, firstNum, secondNum, thirdNum);
    }
}
