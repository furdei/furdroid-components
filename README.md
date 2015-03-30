# Furdroid Components

**Furdroid-components** is a set of light-weight reusable
Android components, such as TintedDrawable support for any API version, PartialLoadingAdapter,
FullCursorAdapter and visual widgets to work with FullCursorAdapter.

## Before you start

Before you start using **furdroid** please make sure you have Android artifacts 'android:android' and
'com.android.support' in your local Maven repository. If you don't please visit
[maven-android-sdk-deployer](https://github.com/simpligility/maven-android-sdk-deployer)
project and follow the instruction.

## Maven Dependency

```xml
<dependency>
  <groupId>systems.furdei</groupId>
  <artifactId>furdroid-components</artifactId>
  <version>1.1.1</version>
</dependency>
```

## Gradle Dependency

```groovy
dependencies {
  compile 'systems.furdei:furdroid-components:1.1.1'
}

```

## Components

### TintedDrawable

*TintedDrawable* implements support for color tinting introduced in Android API 21.
It takes another drawable as a 'pattern' for tinting and a tint color or
a ColorStateList for state-based tinting.

You should provide pattern drawable and tint color or ColorStateList through constructor arguments.
You can also customize tint mode by calling a setTintMode(PorterDuff.Mode) method. The default mode is the most
commonly used PorterDuff.Mode.SRC_IN mode.

This implementation tints drawable resource by changing a pattern's color filter based on the
state of *TintedDrawable* instance. It stores only one copy of the pattern drawable
so does not consume a significant amount of additional memory.

Feel free to use *TintedDrawable* for both API 21 and lower. This implementation
will use API 21 if running on Lollipop devices or higher and fall back to custom implementation
on the lower API versions.

### FullCursorAdapter

*FullCursorAdapter* is an adapter that produces a view for all the rows in a cursor. Unlike the standard Android
Adapter, this implementation creates views for every row in a cursor
at once. It is helpful when you are sure that there will always be a few of rows and your app
will not run out of memory because of creating too many views. Widgets that use FullCursorAdapter
do not provide neither scrolling nor view recycling because all views are always visible.
However you can include such view into a ScrollView just in case some child views wouldn't fit
on some small screens.

### FullCursorListView

*FullCursorListView* is a view that displays a full cursor, e.g. all rows in a cursor.
This implementation displays all items in a plain list.
The list can be either horizontal or vertical. Unlike the standard Android
ListView, this implementation creates views for every row in
a cursor at once. It is helpful when you are sure that there will always be a few of rows and
your app will not run out of memory because of creating too many views.

### PartialLoadingAdapter

*PartialLoadingAdapter* is an adapter for loading large cursors backed by RESTful services
with a local cache in a database. This adapter is responsible for displaying "loading" items
in the end of already loaded items in case when there is at least one more portion of
unfetched data and for calling for that unfetched portion of data either.

Adapter does NOT provide any functionality for loading data itself, calling supplied listener
PartialLoadingListener to start a loading process. loadNextPortionOfData method is called
on the background thread by default unless you change this by calling
setLoadNextPortionOnBackgroundThread(false).

Adapter always tries to load one more portion of data while user is looking through the end
of the already shown portion. The threshold of "the end" can be configured through the
setCursorPositionLoadingThreshold method. The default value is 15, which means that in case
when cursor's length is 50 then the loading starts when the user goes further than 50 - 15 = 35
records. Note that loading doesn't start immediately. It is required that the threshold is
crossed 3 times at least. It prevents us from reacting to "false" views when a ListView
triggers adapter's getView method to determine it's length for example.

## furdroid

**Furdroid-components** is distributed as a part of [furdroid](https://github.com/furdei/furdroid) project.
Follow [this link](https://github.com/furdei/furdroid) to find more useful visual components, widgets and database
tools by [furdei.systems](http://www.furdei.systems).
