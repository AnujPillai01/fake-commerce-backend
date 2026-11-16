package com.example.FakeCommerce.utils;

public class StringUtils {
    public static String toTitleCase(String input){
        if(input == null || input.isBlank()){
            throw new IllegalArgumentException("Input string cannot be empty");
        }

        String[] words = input.trim().toLowerCase().split("\\s+");
        StringBuilder titleCase = new StringBuilder();

        for(String word : words){
          if(!word.isEmpty()){

              titleCase.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1))
                      .append(" ");
          }
        }

        return titleCase.toString().trim();
    }
}
