package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("loadingWheel")
fun goneIfNotNull(view: View, it: Int) {
    view.visibility = if (it != 0) View.GONE else View.VISIBLE
}

@BindingAdapter("pictureUrl")
fun bindUriToImage(imageView: ImageView, strUrl: String?) {
    Picasso.with(imageView.context)
            .load(strUrl)
            .error(R.drawable.placeholder_picture_of_day)
            .into(imageView)
}

@BindingAdapter("emptyTextDesc")
fun bindTextViewToEmptyTextDesc(textView: TextView, strTitle: String?) {
    val context = textView.context

    if (strTitle == null) {
        textView.text = context.getString(R.string.image_of_the_day)
    } else {
        textView.text = strTitle
    }
}

@BindingAdapter("pictureOfDay")
fun bindPictureOfDayImage(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    if (pictureOfDay?.mediaType == "image") {
        Picasso.with(imageView.context)
                .load(pictureOfDay.url)
                .into(imageView)
        imageView.contentDescription = imageView.context.getString(
                R.string.nasa_picture_of_day_content_description_format,
                pictureOfDay.title
        )
    } else {
        imageView.contentDescription = imageView.context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
    }
}

@BindingAdapter("asteroidContentDescription")
fun binContentDescription(ImageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        ImageView.contentDescription = ImageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        ImageView.contentDescription = ImageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}
