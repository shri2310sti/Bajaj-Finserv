package com.bajajfinserv.hiring.service;

import org.springframework.stereotype.Component;

@Component
public class SQLSolver {

    public String solveSQLQuestion(String regNo) {
        // Extract last two digits
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int lastDigit = Integer.parseInt(lastTwoDigits.substring(1));

        // Determine if odd or even
        boolean isOdd = lastDigit % 2 != 0;

        if (isOdd) {
            return getQuestion1Solution();
        } else {
            return getQuestion2Solution();
        }
    }

    private String getQuestion1Solution() {
        // Question 1: Find highest salaried employee per department (excluding 1st day
        // payments)
        return "WITH FilteredPayments AS ( " +
                "SELECT EMP_ID, SUM(AMOUNT) AS TOTAL_SALARY " +
                "FROM PAYMENTS " +
                "WHERE EXTRACT(DAY FROM PAYMENT_TIME) != 1 " +
                "GROUP BY EMP_ID " +
                "), " +
                "RankedEmployees AS ( " +
                "SELECT d.DEPARTMENT_NAME, " +
                "fp.TOTAL_SALARY AS SALARY, " +
                "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS EMPLOYEE_NAME, " +
                "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
                "ROW_NUMBER() OVER (PARTITION BY d.DEPARTMENT_ID ORDER BY fp.TOTAL_SALARY DESC) AS RNK " +
                "FROM FilteredPayments fp " +
                "JOIN EMPLOYEE e ON fp.EMP_ID = e.EMP_ID " +
                "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                ") " +
                "SELECT DEPARTMENT_NAME, SALARY, EMPLOYEE_NAME, AGE " +
                "FROM RankedEmployees " +
                "WHERE RNK = 1";
    }

    private String getQuestion2Solution() {
        // Question 2: Average age and employee list for employees earning > 70,000
        return "WITH HighEarners AS ( " +
                "SELECT e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, e.DOB, e.DEPARTMENT, p.AMOUNT " +
                "FROM EMPLOYEE e " +
                "JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID " +
                "WHERE p.AMOUNT > 70000 " +
                "), " +
                "DepartmentStats AS ( " +
                "SELECT d.DEPARTMENT_ID, " +
                "d.DEPARTMENT_NAME, " +
                "AVG(TIMESTAMPDIFF(YEAR, he.DOB, CURDATE())) AS AVERAGE_AGE, " +
                "GROUP_CONCAT(CONCAT(he.FIRST_NAME, ' ', he.LAST_NAME) ORDER BY he.EMP_ID SEPARATOR ', ') AS EMPLOYEE_LIST "
                +
                "FROM HighEarners he " +
                "JOIN DEPARTMENT d ON he.DEPARTMENT = d.DEPARTMENT_ID " +
                "GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME " +
                ") " +
                "SELECT DEPARTMENT_NAME, " +
                "ROUND(AVERAGE_AGE, 2) AS AVERAGE_AGE, " +
                "SUBSTRING_INDEX(EMPLOYEE_LIST, ', ', 10) AS EMPLOYEE_LIST " +
                "FROM DepartmentStats " +
                "ORDER BY DEPARTMENT_ID DESC";
    }
}