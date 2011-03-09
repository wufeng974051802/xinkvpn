package xink.vpn.editor;

import xink.vpn.Constants;
import xink.vpn.VpnActor;
import xink.vpn.VpnSettings;
import xink.vpn.wrapper.VpnProfile;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class VpnProfileEditor extends Activity {

    private EditAction editAction;
    private VpnProfile profile;
    private EditText txtVpnName;
    private EditText txtServer;
    private EditText txtDnsSuffices;
    private EditText txtUserName;
    private EditText txtPassword;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("VPN Editor");

        Intent intent = getIntent();
        editAction = EditAction.valueOf(intent.getAction());
        initProfile(intent);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        setContentView(content);

        initWidgets(content);
    }

    private void initProfile(final Intent intent) {
        if (editAction == EditAction.CREATE) {
            profile = createProfile();
        } else {
            profile = (VpnProfile) intent.getExtras().get(Constants.KEY_VPN_PROFILE);
        }
    }

    private void initWidgets(final ViewGroup content) {
        TextView lblVpnName = new TextView(this);
        lblVpnName.setText("Name");
        content.addView(lblVpnName);

        txtVpnName = new EditText(this);
        content.addView(txtVpnName);

        TextView lblServer = new TextView(this);
        lblServer.setText("Server");
        content.addView(lblServer);

        txtServer = new EditText(this);
        content.addView(txtServer);

        initSpecificWidgets(content);

        TextView lblDnsSuffices = new TextView(this);
        lblDnsSuffices.setText("DNS Suffices");
        content.addView(lblDnsSuffices);

        TextView lblDnsSufficesDesc = new TextView(this);
        lblDnsSufficesDesc.setText("comma separated");
        lblDnsSufficesDesc.setTextColor(0xFF999999);
        lblDnsSufficesDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        content.addView(lblDnsSufficesDesc);

        txtDnsSuffices = new EditText(this);
        content.addView(txtDnsSuffices);

        TextView lblUserName = new TextView(this);
        lblUserName.setText("User Name");
        content.addView(lblUserName);

        txtUserName = new EditText(this);
        content.addView(txtUserName);

        TextView lblPassword = new TextView(this);
        lblPassword.setText("Password");
        content.addView(lblPassword);

        txtPassword = new EditText(this);
        txtPassword.setTransformationMethod(new PasswordTransformationMethod());
        content.addView(txtPassword);

        LinearLayout ctrlPnl = new LinearLayout(this);
        content.addView(ctrlPnl);

        Button btnSave = new Button(this);
        btnSave.setText("Save");
        ctrlPnl.addView(btnSave);
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                onSave();
            }
        });

        Button btnCancel = new Button(this);
        btnCancel.setText("Cancel");
        ctrlPnl.addView(btnCancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                onCancel();
            }
        });
    }

    protected void onSave() {
        profile.setName(txtVpnName.getText().toString().trim());
        profile.setServerName(txtServer.getText().toString().trim());
        profile.setDomainSuffices(txtDnsSuffices.getText().toString().trim());
        profile.setUsername(txtUserName.getText().toString().trim());
        profile.setPassword(txtPassword.getText().toString().trim());
        populate();

        VpnActor.getInstance().addVpnProfile(profile);
        Intent intent = new Intent(this, VpnSettings.class);
        intent.putExtra(Constants.KEY_VPN_PROFILE, profile.getId());
        setResult(Constants.REQ_ADD_VPN, intent);
        finish();
    }

    protected abstract void populate();

    protected void onCancel() {
        // TODO Auto-generated method stub
        finish();
    }

    protected EditAction getEditAction() {
        return editAction;
    }

    protected abstract void initSpecificWidgets(final ViewGroup content);

    protected abstract VpnProfile createProfile();

    @SuppressWarnings("unchecked")
    protected <T extends VpnProfile> T getProfile() {
        return (T) profile;
    }

}