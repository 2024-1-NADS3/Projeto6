// Generated by view binder compiler. Do not edit!
package com.example.frontend.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.frontend.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityFormLoginBinding implements ViewBinding {
  @NonNull
  private final NestedScrollView rootView;

  @NonNull
  public final AppCompatButton btEntrar;

  @NonNull
  public final View containerComponents;

  @NonNull
  public final EditText email;

  @NonNull
  public final TextView esqueciSenha;

  @NonNull
  public final ImageView logo;

  @NonNull
  public final ProgressBar progressbar;

  @NonNull
  public final EditText senha;

  @NonNull
  public final TextView textTelaCadastro;

  @NonNull
  public final TextView tituloLogin;

  @NonNull
  public final ImageView voltarTelaInicio;

  private ActivityFormLoginBinding(@NonNull NestedScrollView rootView,
      @NonNull AppCompatButton btEntrar, @NonNull View containerComponents, @NonNull EditText email,
      @NonNull TextView esqueciSenha, @NonNull ImageView logo, @NonNull ProgressBar progressbar,
      @NonNull EditText senha, @NonNull TextView textTelaCadastro, @NonNull TextView tituloLogin,
      @NonNull ImageView voltarTelaInicio) {
    this.rootView = rootView;
    this.btEntrar = btEntrar;
    this.containerComponents = containerComponents;
    this.email = email;
    this.esqueciSenha = esqueciSenha;
    this.logo = logo;
    this.progressbar = progressbar;
    this.senha = senha;
    this.textTelaCadastro = textTelaCadastro;
    this.tituloLogin = tituloLogin;
    this.voltarTelaInicio = voltarTelaInicio;
  }

  @Override
  @NonNull
  public NestedScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityFormLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityFormLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_form_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityFormLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bt_entrar;
      AppCompatButton btEntrar = ViewBindings.findChildViewById(rootView, id);
      if (btEntrar == null) {
        break missingId;
      }

      id = R.id.containerComponents;
      View containerComponents = ViewBindings.findChildViewById(rootView, id);
      if (containerComponents == null) {
        break missingId;
      }

      id = R.id.email;
      EditText email = ViewBindings.findChildViewById(rootView, id);
      if (email == null) {
        break missingId;
      }

      id = R.id.esqueci_senha;
      TextView esqueciSenha = ViewBindings.findChildViewById(rootView, id);
      if (esqueciSenha == null) {
        break missingId;
      }

      id = R.id.logo;
      ImageView logo = ViewBindings.findChildViewById(rootView, id);
      if (logo == null) {
        break missingId;
      }

      id = R.id.progressbar;
      ProgressBar progressbar = ViewBindings.findChildViewById(rootView, id);
      if (progressbar == null) {
        break missingId;
      }

      id = R.id.senha;
      EditText senha = ViewBindings.findChildViewById(rootView, id);
      if (senha == null) {
        break missingId;
      }

      id = R.id.text_tela_cadastro;
      TextView textTelaCadastro = ViewBindings.findChildViewById(rootView, id);
      if (textTelaCadastro == null) {
        break missingId;
      }

      id = R.id.titulo_login;
      TextView tituloLogin = ViewBindings.findChildViewById(rootView, id);
      if (tituloLogin == null) {
        break missingId;
      }

      id = R.id.voltar_tela_inicio;
      ImageView voltarTelaInicio = ViewBindings.findChildViewById(rootView, id);
      if (voltarTelaInicio == null) {
        break missingId;
      }

      return new ActivityFormLoginBinding((NestedScrollView) rootView, btEntrar,
          containerComponents, email, esqueciSenha, logo, progressbar, senha, textTelaCadastro,
          tituloLogin, voltarTelaInicio);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
