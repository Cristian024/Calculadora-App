package com.example.myapplication.controllers

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorController (context: Context){
    private val operators = arrayOf("+", "-", "*", "/", "%");
    private val sharedPreferences = SharedPreferencesController(context);
    private val SHARED_HISTORY = "HISTORY";
    private val gson = Gson();

    val operationText = MutableLiveData<String>();
    val history = MutableLiveData<ArrayList<String>>();
    var operation = "";

    fun validateCorrectSyntax(value: String): Boolean{
        if(operators.contains(value)){
            try {
                val lastKey = operation.last().toString();

                if(operation.isEmpty())return false;

                if(operators.contains(lastKey))return false;

                if(lastKey == ".")return false;
            } catch (e: Exception){
                return false;
            }
        }else if(value == "."){
            try{
                val lastKey = operation.last().toString();

                if(operation.isEmpty())return false;

                if(lastKey == ".")return false;

                val numbers = convertNumbersToArray(operation);

                val lastNumber: String = numbers.last();
                if(lastNumber.contains(".")) return false;

            }catch (e: Exception){
                return false
            }
        }else if(isInteger(value)){
            return true;
        } else {
            try {
                val lastKey = operation.last().toString();

                if(operators.contains(lastKey)) return false;
            }catch (e: Exception){
                return false
            }
        }

        return true;
    }

    private fun isInteger(string: String): Boolean{
        return string.toIntOrNull() != null;
    }

    private fun convertNumbersToArray(string: String): List<String> {
        val regex = Regex("-?\\d+(\\.\\d+)?")

        return regex.findAll(string)
            .map { it.value.toString() }
            .toList()
    }

    fun getResultOperation(operation: String): Any {
        return try {
            val result = ExpressionBuilder(operation).build().evaluate();

            val numbers = result.toString().split(".");
            saveIntoHistory(operation);
            if(numbers[1].toInt() <= 0)return numbers[0] else return result;
        }catch (e: Exception){
            "Syntax Error"
        }
    }

    private fun saveIntoHistory(operation: String){
        if(!history.value!!.contains(operation)){
            val his = history.value;
            his?.add(0, operation);
            if(his?.size!! > 20){
                his.removeLast();
            }

            history.postValue(his);
            val historyJson = gson.toJson(his);
            sharedPreferences.setString(SHARED_HISTORY, "HISTORY", historyJson);
        }
    }

    fun getHistory(): ArrayList<String>{
        val history = sharedPreferences.getString(SHARED_HISTORY, "HISTORY");
        /*val historyArray = Array(20) { index -> index * 1 };*/
        val historyArray = arrayListOf<String>();
        if (history != null) {
            if(history.isNotEmpty()){
                val dataJson = gson.fromJson(history, ArrayList::class.java);
                dataJson.forEach {
                    historyArray.add(it.toString());
                }
            }
        }

        return historyArray;
    }

}