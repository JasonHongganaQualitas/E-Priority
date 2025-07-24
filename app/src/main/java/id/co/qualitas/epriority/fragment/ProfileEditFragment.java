package id.co.qualitas.epriority.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentEditProfileBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.User;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditFragment extends BaseFragment {
    private FragmentEditProfileBinding binding;
    private String fullName, phoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        init();
        setData();
        binding.backBtn.setOnClickListener(v -> getActivity().getOnBackPressedDispatcher().onBackPressed());
        binding.btnSave.setOnClickListener(v -> {
            int error = 0;
            if (binding.edtPhoneNumber.getText().toString().isEmpty()) {
                binding.edtPhoneNumber.setError("This value is required.");
                error++;
            }
            if (binding.edtFullName.getText().toString().isEmpty()) {
                binding.edtFullName.setError("This value is required.");
                error++;
            }
            if (error == 0) {
                fullName = binding.edtFullName.getText().toString().trim();
                phoneNumber = binding.edtPhoneNumber.getText().toString().trim();
                editProfile();
            }
        });

        return binding.getRoot();
    }

    private void setData() {
        binding.edtFullName.setText(Helper.isEmpty(user.getName(), ""));
        binding.edtEmail.setText(Helper.isEmpty(user.getEmail(), ""));
        binding.edtPhoneNumber.setText(Helper.isEmpty(user.getPhone(), ""));
    }

    private void editProfile() {
        openDialogProgress();
        User param = new User();
        param.setName(fullName);
        param.setPhoneNumber(phoneNumber);
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.editProfile(param);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            setToast(result.getMessage());
                            user.setPhone(phoneNumber);
                            user.setName(fullName);
                            session.createLoginSession(Helper.objectToString(user));
                            init();
                        } else {
                            openDialogInformation(Constants.DATA_NOT_FOUND, response.message(), null);
                        }
                    } else {
                        openDialogInformation(Constants.DATA_NOT_FOUND, response.message(), null);
                    }
                } else {
                    openDialogInformation(Constants.INTERNAL_SERVER_ERROR, response.message(), null);
                }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                openDialogInformation(Constants.INTERNAL_SERVER_ERROR, t.getMessage(), null);
            }

        });
    }
}