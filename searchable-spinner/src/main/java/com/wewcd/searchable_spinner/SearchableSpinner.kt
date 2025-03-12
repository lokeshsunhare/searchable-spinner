package com.wewcd.searchable_spinner


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView

object SearchableSpinner {
    @JvmStatic
    @SuppressLint("SetTextI18n")
    fun <T> showSearchableSpinnerDialog(
        context: Context,
        titleText: String,
        itemList: List<T>,
        getItemName: (T) -> String,
        getItemId: (T) -> Any, // Function to extract ID
        onItemSelected: (T, Any) -> Unit, // Returns selected item & its ID
        bgColor: Int // Background color parameter
    ) {
        val dialog = Dialog(context).apply {
            setContentView(R.layout.dialog_searchable_spinner)
            window?.apply {
                val displayMetrics = DisplayMetrics()
                @Suppress("DEPRECATION")
                windowManager.defaultDisplay.getRealMetrics(displayMetrics)
                val screenWidth = displayMetrics.widthPixels
                val screenHeight = displayMetrics.heightPixels
                setLayout((screenWidth * 0.80).toInt(), (screenHeight * 0.50).toInt())
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            show()
        }

        val rootLayout: View = dialog.findViewById(R.id.dialog_root) // Root layout of dialog
        rootLayout.setBackgroundColor(bgColor) // Set background color dynamically

        val editText: EditText = dialog.findViewById(R.id.edit_text)
        val tvTitle: TextView = dialog.findViewById(R.id.tvTitle)
        tvTitle.text = "Select $titleText"
        editText.setHint("Search $titleText")
        val listView = dialog.findViewById<ListView>(R.id.list_view)

        // Keep track of filtered items
        val filteredList = mutableListOf<T>().apply { addAll(itemList) }

        val adapter = object : ArrayAdapter<String>(
            context,
            android.R.layout.simple_list_item_1,
            filteredList.map(getItemName)
        ) {
            override fun getItem(position: Int): String = getItemName(filteredList[position])
        }

        listView.adapter = adapter

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
                filteredList.clear()
                filteredList.addAll(itemList.filter { getItemName(it).contains(s!!, true) })
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = filteredList[position]
            val selectedId = getItemId(selectedItem)
            onItemSelected(selectedItem, selectedId) // Pass item & ID
            dialog.dismiss()
        }
    }
}