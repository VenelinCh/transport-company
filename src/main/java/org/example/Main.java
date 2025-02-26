package org.example;

import org.example.Dto.PaymentDTO;
import org.example.configuration.SessionFactoryUtil;
import org.example.dao.*;
import org.example.entity.*;
import org.example.exceptions.CompanyException;
import org.example.services.CalculateCompanyIncome;
import org.example.services.OutputFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        SessionFactoryUtil.getSessionFactory().openSession();
        CalculateCompanyIncome calculate = new CalculateCompanyIncome(1);
        CalculateCompanyIncome calculate1 = new CalculateCompanyIncome(2);
        CalculateCompanyIncome calculate2 = new CalculateCompanyIncome(3);
        Thread thread = calculate;
        Thread thread1 = calculate1;
        Thread thread2 = calculate2;
        thread.start();
        thread1.start();
        thread2.start();
        try{
            thread.join();
            thread1.join();
            thread2.join();
        }catch (InterruptedException e){
            throw new RuntimeException();
        }

        Menu.showMenu0();
    }
}