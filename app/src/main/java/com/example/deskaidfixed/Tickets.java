package com.example.deskaidfixed;

import java.util.ArrayList;

public class Tickets extends Throwable {

    public ArrayList<String> ids = new ArrayList<>();

    private String firstname;
    private String lastname;
    private String email;
    private String subject;
    private String state;
    private String priority;
    private String ticketDesc;
    private String ID;

    public Tickets(){}

    public Tickets(String firstname, String lastname, String email, String subject, String state, String priority, String ticketDesc, String ID){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.subject = subject;
        this.state = state;
        this.priority = priority;
        this.ticketDesc = ticketDesc;
        this.ID = ID;
    }

    public String getFirstname(){
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getState() {
        return state;
    }

    public String getPriority() {
        return priority;
    }

    public String getTicketDesc() {
        return ticketDesc;
    }

    public String getID(){return ID;}


    public Tickets setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public Tickets setState(String state) {
        this.state = state;
        return this;
    }
    public Tickets setFirstName(String firstname) {
        this.firstname = firstname;
        return this;
    }
    public Tickets setLastName (String lastname) {
        this.lastname = lastname;
        return this;
    }
    public Tickets setPriority(String priority) {
        this.priority = priority;
        return this;
    }
    public Tickets setEmail(String email) {
        this.email = email;
        return this;
    }
    public Tickets setTicketDesc(String ticketDesc) {
        this.ticketDesc = ticketDesc;
        return this;
    }

    public Tickets setTicketID(String ID){
        this.ID = ID;
        return this;
    }


}
