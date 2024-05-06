// Generated by view binder compiler. Do not edit!
package com.example.frontend.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.frontend.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class CardCursoBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final CardView cardView;

  @NonNull
  public final TextView courseAddress;

  @NonNull
  public final TextView courseCategory;

  @NonNull
  public final ImageView courseImg;

  @NonNull
  public final TextView courseTitle;

  @NonNull
  public final TextView courseType;

  @NonNull
  public final TextView slotsAndMax;

  @NonNull
  public final TextView textView6;

  private CardCursoBinding(@NonNull CardView rootView, @NonNull CardView cardView,
      @NonNull TextView courseAddress, @NonNull TextView courseCategory,
      @NonNull ImageView courseImg, @NonNull TextView courseTitle, @NonNull TextView courseType,
      @NonNull TextView slotsAndMax, @NonNull TextView textView6) {
    this.rootView = rootView;
    this.cardView = cardView;
    this.courseAddress = courseAddress;
    this.courseCategory = courseCategory;
    this.courseImg = courseImg;
    this.courseTitle = courseTitle;
    this.courseType = courseType;
    this.slotsAndMax = slotsAndMax;
    this.textView6 = textView6;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static CardCursoBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CardCursoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.card_curso, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CardCursoBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      CardView cardView = (CardView) rootView;

      id = R.id.courseAddress;
      TextView courseAddress = ViewBindings.findChildViewById(rootView, id);
      if (courseAddress == null) {
        break missingId;
      }

      id = R.id.courseCategory;
      TextView courseCategory = ViewBindings.findChildViewById(rootView, id);
      if (courseCategory == null) {
        break missingId;
      }

      id = R.id.courseImg;
      ImageView courseImg = ViewBindings.findChildViewById(rootView, id);
      if (courseImg == null) {
        break missingId;
      }

      id = R.id.courseTitle;
      TextView courseTitle = ViewBindings.findChildViewById(rootView, id);
      if (courseTitle == null) {
        break missingId;
      }

      id = R.id.courseType;
      TextView courseType = ViewBindings.findChildViewById(rootView, id);
      if (courseType == null) {
        break missingId;
      }

      id = R.id.slotsAndMax;
      TextView slotsAndMax = ViewBindings.findChildViewById(rootView, id);
      if (slotsAndMax == null) {
        break missingId;
      }

      id = R.id.textView6;
      TextView textView6 = ViewBindings.findChildViewById(rootView, id);
      if (textView6 == null) {
        break missingId;
      }

      return new CardCursoBinding((CardView) rootView, cardView, courseAddress, courseCategory,
          courseImg, courseTitle, courseType, slotsAndMax, textView6);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
