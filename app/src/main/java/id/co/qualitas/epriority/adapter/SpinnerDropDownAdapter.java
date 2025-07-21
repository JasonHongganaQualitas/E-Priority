package id.co.qualitas.epriority.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.rpc.Help;

import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.databinding.SpinnerFilteredItemBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.model.Dropdown;

public class SpinnerDropDownAdapter extends ArrayAdapter<Dropdown> {
    Context context;
    List<Dropdown> mFilteredList;
    LayoutInflater inflater;
    SpinnerFilteredItemBinding binding;

    public SpinnerDropDownAdapter(Context applicationContext, List<Dropdown> mFilteredList) {
        super(applicationContext, 0, mFilteredList);
        this.context = applicationContext;
        this.mFilteredList = mFilteredList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            binding = SpinnerFilteredItemBinding.inflate(inflater, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (SpinnerFilteredItemBinding) convertView.getTag();
        }

        Dropdown currentItem = getItem(position);
        String id = currentItem.getId() + " - ";
        String name = Helper.isEmpty(currentItem.getName(), "");

        binding.text1.setText(name);

        return convertView;
    }

    public void setData(List<Dropdown> mDataSet) {
        this.mFilteredList = mDataSet;
        notifyDataSetChanged();
    }
}