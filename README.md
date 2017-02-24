# Alerter

### General

[ ![Download](https://api.bintray.com/packages/tapadoo/maven/alerter/images/download.svg) ](https://bintray.com/tapadoo/maven/alerter/_latestVersion)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Alerter-blue.svg?style=flat)](https://android-arsenal.com/details/1/5302)

This library aims to overcome the limitations of Toasts and Snackbars, while reducing the
complexity of your layouts.

![Default Alert](./documentation/alert_default.gif)

A customisable Alert view is dynamically added to the Decor View of the Window, overlaying
all content.

## Gradle

```groovy
dependencies {
    compile 'com.tapadoo.android:alerter:1.0.4'
}
```

# Usage

With simplicity in mind, the Alerter employs the builder pattern to facilitate easy integration
into any app.

From an Activity -

```java
Alerter.create(this)
       .setTitle("Alert Title")
       .setText("Alert text...")
       .show();
```

Or from a Fragment -

```java
Alerter.create(getActivity())
       .setTitle("Alert Title")
       .setText("Alert text...")
       .show();
```

# Customisation

### Background Colour

```java
Alerter.create(this)
       .setTitle("Alert Title")
       .setText("Alert text...")
       .setBackgroundColor(R.color.colorAccent)
       .show();
```

![Coloured Alert](./documentation/alert_coloured.gif)

### Icon

```java
Alerter.create(this)
       .setText("Alert text...")
       .setIcon(R.drawable.ic_face)
       .show();
```

![Custom Icon Alert](./documentation/alert_icon.gif)

### On screen duration, in milliseconds

```java
Alerter.create(this)
       .setTitle("Alert Title")
       .setText("Alert text...")
       .setDuration(10000)
       .show();
```

### Without title

```java
Alerter.create(this)
       .setText("Alert text...")
       .show();
```

![Text Only Alert](./documentation/alert_text_only.gif)

### Adding an On Click Listener

```java
 Alerter.create(ExampleActivity.this)
        .setTitle("Alert Title")
        .setText("Alert text...")
        .setDuration(10000)
        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ExampleActivity.this, "OnClick Called", Toast.LENGTH_LONG).show();
            }
        })
        .show();
```

![On Click Alert](./documentation/alert_on_click.gif)

### Verbose text

```java
 Alerter.create(ExampleActivity.this)
        .setTitle("Alert Title")
        .setText("The alert scales to accommodate larger bodies of text. " +
                 "The alert scales to accommodate larger bodies of text. " +
                 "The alert scales to accommodate larger bodies of text.")
        .show();
```

![Verbose Alert](./documentation/alert_verbose.gif)

### Visibility Callbacks

```java
 Alerter.create(ExampleActivity.this)
        .setTitle("Alert Title")
        .setOnShowListener(new OnShowAlertListener() {
            @Override
            public void onShow() {
                Toast.makeText(ExampleActivity.this, "Alert Shown", Toast.LENGTH_LONG).show();
            }
        })
        .setOnHideListener(new OnHideAlertListener() {
            @Override
            public void onHide() {
                Toast.makeText(ExampleActivity.this, "Alert Hidden", Toast.LENGTH_LONG).show();
            }
         })
        .show();
```

## Sample

Clone this repo and check out the `app` module.

## Licence

See the [LICENSE](LICENSE.md) file for license rights and limitations (MIT).

Copyright 2016 Tapadoo, Dublin.

![Alt Text](http://tapadoo.com/wp-content/themes/tapadoo/img/tapadoo-logo@2x.png)






