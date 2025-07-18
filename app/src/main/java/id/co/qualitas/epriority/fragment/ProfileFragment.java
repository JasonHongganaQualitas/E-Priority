package id.co.qualitas.epriority.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.activity.MainActivity;
import id.co.qualitas.epriority.activity.SignUpActivity;
import id.co.qualitas.epriority.adapter.BookingHistoryAdapter;
import id.co.qualitas.epriority.adapter.OnGoingTripAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentProfileBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.Booking;
import id.co.qualitas.epriority.model.Employee;
import id.co.qualitas.epriority.model.User;
import id.co.qualitas.epriority.model.WSMessage;
import id.co.qualitas.epriority.session.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends BaseFragment {
    private FragmentProfileBinding binding;
    View view;
    OnGoingTripAdapter adapter;
    private List<Booking> mList = new ArrayList<>();
    boolean showPassword = false, showConfirmPassword = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        init();
        initialize();
        initAdapter();
        setData();
        if (user.isFromGoogle()){
            binding.llChangePassword.setVisibility(View.GONE);
        }
        binding.btnEditProfile.setOnClickListener(v -> {
            ProfileEditFragment fragment = new ProfileEditFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });
        binding.llChangePassword.setOnClickListener(v -> {
            dialogChangePass();
        });
        return binding.getRoot();
    }

    private void dialogChangePass() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText oldPassEdit = dialogView.findViewById(R.id.oldPassEdit);
        EditText newPassEdit = dialogView.findViewById(R.id.newPassEdit);
        EditText confPassEdit = dialogView.findViewById(R.id.confPassEdit);
        ImageView imgShowPassword = dialogView.findViewById(R.id.imgShowPassword);
        ImageView imgShowConfirmPassword = dialogView.findViewById(R.id.imgShowConfirmPassword);
        ImageView imgShowOldPassword = dialogView.findViewById(R.id.imgShowOldPassword);
        TextView btnSave = dialogView.findViewById(R.id.btnSave);

        imgShowOldPassword.setOnClickListener(view -> {
            if (!showPassword) {
                //show password
                oldPassEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imgShowOldPassword.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pass_hide));
                showPassword = true;
            } else {
                // hide password
                oldPassEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgShowOldPassword.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pass_show));
                showPassword = false;
            }
            oldPassEdit.setSelection(Helper.isEmpty(oldPassEdit) ? 0 : oldPassEdit.getText().length());
        });

        imgShowPassword.setOnClickListener(view -> {
            if (!showPassword) {
                //show password
                newPassEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imgShowPassword.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pass_hide));
                showPassword = true;
            } else {
                // hide password
                newPassEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgShowPassword.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pass_show));
                showPassword = false;
            }
            newPassEdit.setSelection(Helper.isEmpty(newPassEdit) ? 0 : newPassEdit.getText().length());
        });

        imgShowConfirmPassword.setOnClickListener(view -> {
            if (!showConfirmPassword) {
                //show password
                confPassEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imgShowConfirmPassword.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pass_hide));
                showConfirmPassword = true;
            } else {
                // hide password
                confPassEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgShowConfirmPassword.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pass_show));
                showConfirmPassword = false;
            }
            confPassEdit.setSelection(Helper.isEmpty(confPassEdit) ? 0 : confPassEdit.getText().length());
        });

        btnSave.setOnClickListener(v -> {
            Employee employee = new Employee();
            employee.setOldPassword(oldPassEdit.getText().toString());
            employee.setNewPassword(newPassEdit.getText().toString());

            if (oldPassEdit.getText().toString().equals(newPassEdit.getText().toString())){
                setToast("New password cannot be the same with old password");
                dialog.dismiss();
                return;
            }
            if (!newPassEdit.getText().toString().equals(confPassEdit.getText().toString())){
                setToast("Confirm password and new password must be the same");
                dialog.dismiss();
                return;
            }
            if (user.isFromGoogle()){
                setToast("Cannot change password with google sign in");
                dialog.dismiss();
                return;
            }

            apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
            Call<WSMessage> httpRequest = apiInterface.changePassword(employee);
            httpRequest.enqueue(new Callback<WSMessage>() {
                @Override
                public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                    if (response.isSuccessful()){
                        WSMessage result = response.body();
                        if (result != null){
                            if (result.getIdMessage() == 1) {
                                setToast(result.getMessage());
                                ((MainActivity) getActivity()).logOut();
                            } else {
                                setToast(result.getMessage());
                            }
                        }
                        else {
                            setToast("Failed");
                        }
                    }
                    else {
                        setToast("Failed");
                    }
                }

                @Override
                public void onFailure(Call<WSMessage> call, Throwable t) {

                }
            });
        });

        dialog.show();
    }

    private void setData() {
        binding.txtName.setText(Helper.isEmpty(user.getName(), ""));
        binding.txtEmail.setText(Helper.isEmpty(user.getEmail(), ""));
    }

    private void initAdapter() {
        binding.bookingHistoryRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OnGoingTripAdapter(ProfileFragment.this, mList, (header, pos) -> {
        });
        binding.bookingHistoryRV.setAdapter(adapter);

        if (Helper.isNotEmptyOrNull(mList)) {
            binding.bookingHistoryRV.setVisibility(View.VISIBLE);
            binding.noTripLL.setVisibility(View.GONE);
        } else {
            binding.bookingHistoryRV.setVisibility(View.GONE);
            binding.noTripLL.setVisibility(View.VISIBLE);
        }
    }

    private void initialize() {
        binding.lLogout.setOnClickListener(v -> ((MainActivity) getActivity()).callLogout());
    }
}