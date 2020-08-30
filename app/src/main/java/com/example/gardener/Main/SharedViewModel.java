package com.example.gardener.Main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gardener.Model.Shift;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Shift>> text = new MutableLiveData<>();
    public void setText(ArrayList<Shift> input) {
        text.setValue(input);
    }
    public LiveData<ArrayList<Shift>> getText() {
        return text;
    }


}
