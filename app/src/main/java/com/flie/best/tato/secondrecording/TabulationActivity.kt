package com.flie.best.tato.secondrecording

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.flie.best.tato.secondrecording.databinding.ActivityListRecordBinding
import com.flie.best.tato.secondrecording.databinding.ActivityMainBinding
import com.flie.best.tato.secondrecording.databinding.ActivityTranscribeBinding

class TabulationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListRecordBinding
    private lateinit var listAdapter: TabAdapter
    private lateinit var listData: MutableList<AutoBean>
    private var positionItem: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initList()
        clickFun()
    }

    private fun clickFun() {
        binding.imageView2.setOnClickListener {
            finish()
        }
        binding.tvSave.setOnClickListener {
            saveRename()
        }
        binding.tvCancel.setOnClickListener {
            dismissRename()
        }
    }

    private fun initList() {
        listData = SjUtils.getRecordingsFromStorage(this)
        isListEmpty()
        listAdapter = TabAdapter(listData)
        binding.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.recyclerView.adapter = listAdapter
        listAdapter.setOnItemClickListener(object : TabAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if(binding.clRename.visibility != android.view.View.VISIBLE){
                    jumpToDetail(position)
                }
            }

            override fun onItemNameClick(position: Int) {
                positionItem = position
                binding.clRename.visibility = android.view.View.VISIBLE
                binding.editText.requestFocus()
                binding.editText.findFocus()
                binding.editText.postDelayed({
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(binding.editText, 0)
                }, 120)
            }

            override fun onItemDeleteClick(position: Int) {
                if(binding.clRename.visibility != android.view.View.VISIBLE){
                    showDeleteDialog(position)
                }
            }
        })
    }

    private fun isListEmpty() {
        if (listData.isEmpty()) {
            binding.tvNoRecord.visibility = android.view.View.VISIBLE
            binding.recyclerView.visibility = android.view.View.GONE
        } else {
            binding.tvNoRecord.visibility = android.view.View.GONE
            binding.recyclerView.visibility = android.view.View.VISIBLE
        }
    }

    private fun showDeleteDialog(position: Int) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Tip")
            .setMessage("Deletion is irreversible. Are you sure you want to proceed with the deletion?")
            .setPositiveButton("Confirm") { _, _ ->
                SjUtils.deleteFileByName(this, listData[position].name)
                listData = SjUtils.getRecordingsFromStorage(this)
                listAdapter.updateData(listData)
                isListEmpty()
                dialog.create().dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
        dialog.show()
    }

    fun jumpToDetail(position: Int) {
        val intent = Intent(this, PlayActivity::class.java)
        intent.putExtra("autoName", listData[position].name)
        intent.putExtra("autoTime", listData[position].duration)
        startActivity(intent)
    }

    private fun dismissRename() {
        binding.clRename.visibility = android.view.View.GONE
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editText.windowToken, 0)
        binding.editText.text.clear()
    }

    private fun saveRename() {
        val newName = binding.editText.text.toString()
        if (newName.isEmpty()) {
            return
        }
        SjUtils.renameFileByName(this, listData[positionItem].name, newName)
        listData = SjUtils.getRecordingsFromStorage(this)
        listAdapter.updateData(listData)
        dismissRename()
    }
}