// Generated by view binder compiler. Do not edit!
package com.example.frontend.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.frontend.R;
import com.google.android.material.appbar.AppBarLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityPerfilPaceiroBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppBarLayout AppBarLayoutTelaPerfilParceiro;

  @NonNull
  public final Toolbar ToolbarTelaPerfilParceiro;

  @NonNull
  public final Button button3;

  @NonNull
  public final Button button5;

  @NonNull
  public final Button button8;

  @NonNull
  public final View containerComponents;

  @NonNull
  public final TextView errorPartnerTextView;

  @NonNull
  public final Button icUsuario;

  @NonNull
  public final ProgressBar progressBarPerfilParceiro;

  @NonNull
  public final RecyclerView recyclerViewPartner;

  @NonNull
  public final SearchView searchBarPerfilParceiro;

  @NonNull
  public final TextView textView9;

  @NonNull
  public final TextView tituloPerfilParceiro;

  private ActivityPerfilPaceiroBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppBarLayout AppBarLayoutTelaPerfilParceiro,
      @NonNull Toolbar ToolbarTelaPerfilParceiro, @NonNull Button button3, @NonNull Button button5,
      @NonNull Button button8, @NonNull View containerComponents,
      @NonNull TextView errorPartnerTextView, @NonNull Button icUsuario,
      @NonNull ProgressBar progressBarPerfilParceiro, @NonNull RecyclerView recyclerViewPartner,
      @NonNull SearchView searchBarPerfilParceiro, @NonNull TextView textView9,
      @NonNull TextView tituloPerfilParceiro) {
    this.rootView = rootView;
    this.AppBarLayoutTelaPerfilParceiro = AppBarLayoutTelaPerfilParceiro;
    this.ToolbarTelaPerfilParceiro = ToolbarTelaPerfilParceiro;
    this.button3 = button3;
    this.button5 = button5;
    this.button8 = button8;
    this.containerComponents = containerComponents;
    this.errorPartnerTextView = errorPartnerTextView;
    this.icUsuario = icUsuario;
    this.progressBarPerfilParceiro = progressBarPerfilParceiro;
    this.recyclerViewPartner = recyclerViewPartner;
    this.searchBarPerfilParceiro = searchBarPerfilParceiro;
    this.textView9 = textView9;
    this.tituloPerfilParceiro = tituloPerfilParceiro;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityPerfilPaceiroBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityPerfilPaceiroBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_perfil_paceiro, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityPerfilPaceiroBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.AppBarLayout_TelaPerfilParceiro;
      AppBarLayout AppBarLayoutTelaPerfilParceiro = ViewBindings.findChildViewById(rootView, id);
      if (AppBarLayoutTelaPerfilParceiro == null) {
        break missingId;
      }

      id = R.id.Toolbar_TelaPerfilParceiro;
      Toolbar ToolbarTelaPerfilParceiro = ViewBindings.findChildViewById(rootView, id);
      if (ToolbarTelaPerfilParceiro == null) {
        break missingId;
      }

      id = R.id.button3;
      Button button3 = ViewBindings.findChildViewById(rootView, id);
      if (button3 == null) {
        break missingId;
      }

      id = R.id.button5;
      Button button5 = ViewBindings.findChildViewById(rootView, id);
      if (button5 == null) {
        break missingId;
      }

      id = R.id.button8;
      Button button8 = ViewBindings.findChildViewById(rootView, id);
      if (button8 == null) {
        break missingId;
      }

      id = R.id.containerComponents;
      View containerComponents = ViewBindings.findChildViewById(rootView, id);
      if (containerComponents == null) {
        break missingId;
      }

      id = R.id.errorPartnerTextView;
      TextView errorPartnerTextView = ViewBindings.findChildViewById(rootView, id);
      if (errorPartnerTextView == null) {
        break missingId;
      }

      id = R.id.ic_usuario;
      Button icUsuario = ViewBindings.findChildViewById(rootView, id);
      if (icUsuario == null) {
        break missingId;
      }

      id = R.id.progressBarPerfilParceiro;
      ProgressBar progressBarPerfilParceiro = ViewBindings.findChildViewById(rootView, id);
      if (progressBarPerfilParceiro == null) {
        break missingId;
      }

      id = R.id.recyclerViewPartner;
      RecyclerView recyclerViewPartner = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewPartner == null) {
        break missingId;
      }

      id = R.id.searchBarPerfilParceiro;
      SearchView searchBarPerfilParceiro = ViewBindings.findChildViewById(rootView, id);
      if (searchBarPerfilParceiro == null) {
        break missingId;
      }

      id = R.id.textView9;
      TextView textView9 = ViewBindings.findChildViewById(rootView, id);
      if (textView9 == null) {
        break missingId;
      }

      id = R.id.titulo_PerfilParceiro;
      TextView tituloPerfilParceiro = ViewBindings.findChildViewById(rootView, id);
      if (tituloPerfilParceiro == null) {
        break missingId;
      }

      return new ActivityPerfilPaceiroBinding((ConstraintLayout) rootView,
          AppBarLayoutTelaPerfilParceiro, ToolbarTelaPerfilParceiro, button3, button5, button8,
          containerComponents, errorPartnerTextView, icUsuario, progressBarPerfilParceiro,
          recyclerViewPartner, searchBarPerfilParceiro, textView9, tituloPerfilParceiro);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
