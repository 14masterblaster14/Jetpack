package com.example.navigations1

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.widget.RemoteViews
import androidx.navigation.NavDeepLinkBuilder

/**
 * App Widget that deep links you to the [DeepLinkFragment].
 */

class DeepLinkAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val remoteViews = RemoteViews(
            context.packageName,
            R.layout.deep_link_appwidget
        )

        val args = Bundle()
        args.putString("myarg", "From Widget")
        // TODO STEP 10 - construct and set a PendingIntent using DeepLinkBuilder

        //  By default NavDeepLinkBuilder will start your launcher Activity.
        //  You can override this behavior by passing in an activity as the context or
        //  set an explicit activity class via setComponentName().

        val pendingIntent = NavDeepLinkBuilder(context)
                .setGraph(R.navigation.navigation)
                .setDestination(R.id.deeplink_dest)
                .setArguments(args)
                .createPendingIntent()

        remoteViews.setOnClickPendingIntent(R.id.deep_link_button, pendingIntent)
        // TODO END STEP 10
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews)
    }
}