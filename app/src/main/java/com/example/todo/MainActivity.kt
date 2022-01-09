package com.example.todo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.EditText
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                // 1. Remove the item from the list
                listOfTasks.removeAt(position)

                // 2. Notify the adapter that out data has changed
                adapter.notifyDataSetChanged()

                // 3. Save items
                saveItems()
            }
        }

        // 1. Detect when the user clicks on add button
//        findViewById<Button>(R.id.button).setOnClickListener{
//            // Code for when user clicks on button
//            Log.i("Chi", "User clicked on button") // <- debug
//        }

        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)
        // That's all!

        // Set up the button and input field, so the user can enter their task and add to their list

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Set a reference to the button and set onClickListener
        findViewById<Button>(R.id.button).setOnClickListener{
            // 1. Grab the text the user ha input into @id/addTaskField
            val userInput = inputTextField.text.toString()

            // 2. Add the string to the list of tasks: listOfTasks
            listOfTasks.add(userInput)

            // Notify adapter that data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // 3. Reset the text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // Save the data that user has input

    // Save data by writing and reading from a file

    // Get the file we need
    fun getDataFile() : File {
        // Every line is going to represent a specific task in the list of tasks
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every item in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    // Save items by writing them into the data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
}