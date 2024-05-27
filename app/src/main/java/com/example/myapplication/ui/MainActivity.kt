package com.example.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.myapplication.controllers.CalculatorController
import com.example.myapplication.controllers.SharedPreferencesController
import com.example.myapplication.controllers.ThemeController
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;
    private lateinit var themeController: ThemeController
    private lateinit var controller: CalculatorController;
    private var theme = 0;

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(layoutInflater);
        themeController = ThemeController(this);
        controller = CalculatorController(this);

        getThemeMode()
        setContentView(binding.root);
        getHistory();
        setListeners();
        initComponents();
    }

    private fun initComponents(){
        BottomSheetBehavior.from(binding.bottomHistory).apply {
            peekHeight = 0;
            this.state = BottomSheetBehavior.STATE_COLLAPSED;
            this.isDraggable = false;
        }
    }

    private fun getHistory(){
        val history = controller.getHistory();
        controller.history.postValue(history);
        controller.history.observe(this, Observer {
            val arrayAdapter = ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, history
            )

            binding.historyList.adapter = arrayAdapter;

            binding.historyList.setOnItemClickListener { adapterView, view, i, l ->
                controller.operation = history[i];
                controller.operationText.postValue(history[i]);
            }
        })
    }

    private fun setThemeMod(){
        if(theme == AppCompatDelegate.MODE_NIGHT_YES) themeController.setTheme(true);
        if(theme == AppCompatDelegate.MODE_NIGHT_NO) themeController.setTheme(false);
        getThemeMode();
    }

    private fun getThemeMode(){
        val nightMode = themeController.getTheme();

        if(!nightMode) theme = AppCompatDelegate.MODE_NIGHT_YES;
        if(nightMode) theme = AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(theme);
    }

    private fun setListeners(){
        binding.emptyButton.setOnClickListener{
            controller.operation = "";
            controller.operationText.postValue(controller.operation);
            binding.operationsText.text = "";
        };
        binding.deleteButton.setOnClickListener{
            controller.operation = controller.operation.dropLast(1);
            controller.operationText.postValue(controller.operation);
        };
        binding.percentButton.setOnClickListener{writeInOperation("%")};
        binding.divisionButton.setOnClickListener{writeInOperation("/")};
        binding.sevenButton.setOnClickListener{writeInOperation("7")};
        binding.eightButton.setOnClickListener{writeInOperation("8")};
        binding.nineButton.setOnClickListener{writeInOperation("9")};
        binding.multiplyButton.setOnClickListener{writeInOperation("*")};
        binding.fourButton.setOnClickListener{writeInOperation("4")};
        binding.fiveButton.setOnClickListener{writeInOperation("5")};
        binding.sixButton.setOnClickListener{writeInOperation("6")};
        binding.minusButton.setOnClickListener{writeInOperation("-")};
        binding.oneButton.setOnClickListener{writeInOperation("1")};
        binding.twoButton.setOnClickListener{writeInOperation("2")};
        binding.threeButton.setOnClickListener{writeInOperation("3")};
        binding.sumButton.setOnClickListener{writeInOperation("+")};
        binding.doubleZeroButton.setOnClickListener{writeInOperation("00")};
        binding.zeroButton.setOnClickListener{writeInOperation("0")};
        binding.dotButton.setOnClickListener{writeInOperation(".")};

        binding.equalsButton.setOnClickListener { resultOperation() }

        controller.operationText.observe(this, Observer {
            binding.operationResultText.text = it;
        })

        binding.iconTheme.setOnClickListener {
            setThemeMod()
        }

        binding.iconMenu.setOnClickListener{
            toggleShowSheet()
        }

        binding.closeSheet.setOnClickListener{
            toggleShowSheet()
        }
    }

    private fun toggleShowSheet(){
        val bottomSheet = BottomSheetBehavior.from(binding.bottomHistory)
        var state = 0;
        if(bottomSheet.state == BottomSheetBehavior.STATE_COLLAPSED) {
            state = BottomSheetBehavior.STATE_EXPANDED
        }else if(bottomSheet.state == BottomSheetBehavior.STATE_EXPANDED) {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bottomSheet.apply {
            this.state = state;
        }
    }

    private fun writeInOperation(value: String){
        binding.operationsText.text = "";
        if(controller.validateCorrectSyntax(value)){
            controller.operation += value;
            controller.operationText.postValue(controller.operation);
        }
    }

    private fun resultOperation(){
        if(controller.validateCorrectSyntax("")){
            val result = controller.getResultOperation(controller.operation);
            binding.operationsText.text = controller.operation;
            controller.operation = "";
            controller.operationText.postValue(result.toString());
        }
    }
}