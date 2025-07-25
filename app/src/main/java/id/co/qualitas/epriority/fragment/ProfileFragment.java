package id.co.qualitas.epriority.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.activity.BookingDetailsActivity;
import id.co.qualitas.epriority.activity.CreatePassengerActivity;
import id.co.qualitas.epriority.activity.MainActivity;
import id.co.qualitas.epriority.adapter.OnGoingTripAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.DialogChangePasswordBinding;
import id.co.qualitas.epriority.databinding.DialogChooseNationalityBinding;
import id.co.qualitas.epriority.databinding.FragmentProfileBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.TripRequest;
import id.co.qualitas.epriority.model.TripsResponse;
import id.co.qualitas.epriority.model.User;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends BaseFragment {
    private FragmentProfileBinding binding;
    View view;
    OnGoingTripAdapter adapter;
    private List<TripsResponse> mList = new ArrayList<>();
    boolean showPassword = false, showConfirmPassword = false;
    int offset;
    private boolean loading = true;
    protected int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager linearLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        init();
        initialize();
        offset = 0;
        initAdapter();
        getHistory();//onCreateView
        setData();

        if (user.isFromGoogle()) {
            binding.llChangePassword.setVisibility(View.GONE);
        }
        binding.btnEditProfile.setOnClickListener(v -> {
            ProfileEditFragment fragment = new ProfileEditFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });
        binding.llChangePassword.setOnClickListener(v -> {
            dialogChangePass();
        });

        binding.bookingHistoryRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (offset == 0) {
                    itemCount();
                } else {
                    if (dy > 0) //check for scroll down
                    {
                        itemCount();
                    } else {
                        loading = true;
                    }
                }
            }
        });
        return binding.getRoot();
    }

    private void dialogChangePass() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;

        DialogChangePasswordBinding dialogBinding = DialogChangePasswordBinding.inflate(LayoutInflater.from(getContext()));
        dialog = new Dialog(getContext());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialogBinding.imgShowOldPassword.setOnClickListener(view -> {
            if (!showPassword) {
                //show password
                dialogBinding.oldPassEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                dialogBinding.imgShowOldPassword.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pass_hide));
                showPassword = true;
            } else {
                // hide password
                dialogBinding.oldPassEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                dialogBinding.imgShowOldPassword.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pass_show));
                showPassword = false;
            }
            dialogBinding.oldPassEdit.setSelection(Helper.isEmpty(dialogBinding.oldPassEdit) ? 0 : dialogBinding.oldPassEdit.getText().length());
        });

        dialogBinding.imgShowPassword.setOnClickListener(view -> {
            if (!showPassword) {
                //show password
                dialogBinding.newPassEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                dialogBinding.imgShowPassword.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pass_hide));
                showPassword = true;
            } else {
                // hide password
                dialogBinding.newPassEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                dialogBinding.imgShowPassword.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pass_show));
                showPassword = false;
            }
            dialogBinding.newPassEdit.setSelection(Helper.isEmpty(dialogBinding.newPassEdit) ? 0 : dialogBinding.newPassEdit.getText().length());
        });

        dialogBinding.imgShowConfirmPassword.setOnClickListener(view -> {
            if (!showConfirmPassword) {
                //show password
                dialogBinding.confPassEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                dialogBinding.imgShowConfirmPassword.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pass_hide));
                showConfirmPassword = true;
            } else {
                // hide password
                dialogBinding.confPassEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                dialogBinding.imgShowConfirmPassword.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pass_show));
                showConfirmPassword = false;
            }
            dialogBinding.confPassEdit.setSelection(Helper.isEmpty(dialogBinding.confPassEdit) ? 0 : dialogBinding.confPassEdit.getText().length());
        });

        dialogBinding.btnSave.setOnClickListener(v -> {
            int error = 0;
            if (dialogBinding.oldPassEdit.getText().toString().equals(dialogBinding.newPassEdit.getText().toString())) {
                dialogBinding.newPassEdit.setError("New password cannot be the same with old password");
                error++;
            }
            if (!dialogBinding.newPassEdit.getText().toString().equals(dialogBinding.confPassEdit.getText().toString())) {
                dialogBinding.newPassEdit.setError("Confirm password and new password must be the same");
                error++;
            }
            if (user.isFromGoogle()) {
                setToast("Cannot change password with google sign in");
                error++;
            }
            if (error == 0) {
                User param = new User();
                param.setOldPassword(dialogBinding.oldPassEdit.getText().toString());
                param.setNewPassword(dialogBinding.newPassEdit.getText().toString());
                dialog.dismiss();
                changePassword(param);
            }
        });
    }

    private void changePassword(User param) {
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.changePassword(param);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            setToast(result.getMessage());
                            ((MainActivity) getActivity()).logOut();
                        } else {
                            setToast(result.getMessage());
                        }
                    } else {
                        setToast(Constants.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    setToast(Constants.INTERNAL_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                dialog.dismiss();
                setToast(t.getMessage());
            }
        });
    }

    private void setData() {
        binding.txtName.setText(Helper.isEmpty(user.getName(), ""));
        binding.txtEmail.setText(Helper.isEmpty(user.getEmail(), ""));
    }

    public void itemCount() {
        visibleItemCount = linearLayout.getChildCount();
        totalItemCount = linearLayout.getItemCount();
        pastVisiblesItems = linearLayout.findFirstVisibleItemPosition();

        if (loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loading = false;
                offset = totalItemCount;
                getHistory();//itemCount
            }
        }
    }

    private void initAdapter() {
        linearLayout = new LinearLayoutManager(getContext());
        binding.bookingHistoryRV.setLayoutManager(linearLayout);
        adapter = new OnGoingTripAdapter(ProfileFragment.this, mList, (header, pos) -> {
            Helper.setItemParam(Constants.TRIP_HEADER, header);
            Helper.setItemParam(Constants.FROM_HISTORY, 1);
            Intent intent = new Intent(getContext(), BookingDetailsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
        binding.bookingHistoryRV.setAdapter(adapter);
    }

    private void setList(){
        adapter.setFilteredList(mList);
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

    public void getHistory() {
        binding.progressBar.setVisibility(View.VISIBLE);
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        TripRequest tripRequest = new TripRequest();
        tripRequest.setLimit(Integer.parseInt(Constants.DEFAULT_LIMIT));
        tripRequest.setOffset(offset);
        tripRequest.setSearch("");
        Call<WSMessage> httpRequest = apiInterface.getHistory(tripRequest);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.loadingDataBottom.relativeProgress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            Type listType = new TypeToken<ArrayList<TripsResponse>>() {
                            }.getType();
                            List<TripsResponse> tempList = new Gson().fromJson(jsonInString, listType);
                            if (Helper.isNotEmptyOrNull(tempList)) {
                                if (offset == 0) {
                                    mList = new ArrayList<>();
                                }
                                mList.addAll(tempList);
                            }
                        }
                    }
                }
                setList();//onResponse
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                binding.progressBar.setVisibility(View.GONE);
                binding.loadingDataBottom.relativeProgress.setVisibility(View.GONE);
                setList();//onFailure
            }
        });
    }
}