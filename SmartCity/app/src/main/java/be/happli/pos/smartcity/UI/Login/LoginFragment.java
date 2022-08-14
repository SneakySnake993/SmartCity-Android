package be.happli.pos.smartcity.UI.Login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import be.happli.pos.smartcity.Model.User;
import be.happli.pos.smartcity.R;
import be.happli.pos.smartcity.UI.InformationDialog;
import be.happli.pos.smartcity.Utils.Constants;
import be.happli.pos.smartcity.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;
    private String token;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button loginBtn;
    private Button logoutBtn;
    private AccountViewModel accountViewModel;
    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
        accountViewModel = new ViewModelProvider(getActivity()).get(AccountViewModel.class);
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editTextUsername = root.findViewById(R.id.editTextUsername);
        editTextPassword = root.findViewById(R.id.editTextPassword);
        loginBtn = root.findViewById(R.id.loginBtn);
        logoutBtn = root.findViewById(R.id.logoutBtn);
        setLoginBtn();
        setLogoutBtn();

        sharedPreferences = getContext().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, null);

        if(token != null) {
            loginBtn.setEnabled(false);
            logoutBtn.setEnabled(true);
        } else {
            loginBtn.setEnabled(true);
            logoutBtn.setEnabled(false);
        }

        accountViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null) {
                    NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                    View header = navigationView.getHeaderView(0);
                    TextView tvNavHeader = header.findViewById(R.id.tv_nav_header);
                    tvNavHeader.setText(getText(R.string.welcome_nav_header_title) + " " + user.getUsername());
                    //Navigation.findNavController(container).navigate(R.id.action_nav_login_to_nav_bookmarks);
                }
            }
        });

        loginViewModel.getToken().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                accountViewModel.getUserFromToken(s);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);
                sharedPreferences.edit().putString(Constants.TOKEN, s).apply();
                logoutBtn.setEnabled(true);
            }
        });

        loginViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if(error != null) {
                InformationDialog informationDialog = InformationDialog.getInstance();
                informationDialog.setInformation(R.string.error, error.getErrorMessage());
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
                loginBtn.setEnabled(true);
            }
        });

        loginViewModel.getStatusCode().observe(getViewLifecycleOwner(), code -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            if(code == 404) {
                informationDialog.setInformation(R.string.error, R.string.wrong_email_password);
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
                loginBtn.setEnabled(true);
            } else if(code == 500) {
                informationDialog.setInformation(R.string.error, R.string.error_servor);
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
                loginBtn.setEnabled(true);
            }
        });

        return root;
    }

    public void setLoginBtn() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameText = editTextUsername.getText().toString();
                String passwordText = editTextPassword.getText().toString();
                editTextUsername.setText("");
                editTextPassword.setText("");
                editTextUsername.clearFocus();
                editTextPassword.clearFocus();
                loginBtn.setEnabled(false);
                loginViewModel.login(usernameText, passwordText);
            }
        });
    }

    public void setLogoutBtn() {
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setEnabled(true);
                logoutBtn.setEnabled(false);
                editTextUsername.clearFocus();
                editTextPassword.clearFocus();
//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);
//                sharedPreferences.edit().putString(Constants.TOKEN, null).apply();
                removeToken();
                NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                View header = navigationView.getHeaderView(0);
                TextView tvNavHeader = header.findViewById(R.id.tv_nav_header);
                tvNavHeader.setText(getText(R.string.default_nav_header_title));
                accountViewModel.resetUser();
            }
        });
    }

    public void removeToken(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);;
        if(sharedPreferences.contains("token")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("token").apply();
            token = "";
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}