package com.jonbott.learningrxjava.Activities.DatabaseExample

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.jonbott.learningrxjava.Activities.DatabaseExample.Recycler.PhotoDescriptionViewAdapter
import com.jonbott.learningrxjava.Common.disposedBy
import com.jonbott.learningrxjava.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_database_example.*


class DatabaseExampleActivity : AppCompatActivity() {

    private val presenter = DatabaseExamplePresenter()
    private var bag = CompositeDisposable()

    lateinit var adapter: PhotoDescriptionViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_example)

        presenter.photoDescriptions.observeOn(AndroidSchedulers.mainThread())
                .subscribe { descriptions ->
                    descriptions.forEach { println("$it") }
                    adapter.photoDescriptions.accept(descriptions.toMutableList())
                }.disposedBy(bag)

        attachUI()
    }

    private fun attachUI() {
        val linearLayoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        photoDescriptionRecyclerView.setHasFixedSize(true)
        photoDescriptionRecyclerView.layoutManager = linearLayoutManager
        photoDescriptionRecyclerView.addItemDecoration(dividerItemDecoration)

        initializeListView()

        et_title.setOnEditorActionListener { textView, action: Int, keyEvent ->
            if (action == EditorInfo.IME_ACTION_DONE || action == EditorInfo.IME_ACTION_DONE
                    || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                    && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                //submit the form
                submitForm()
            }
            return@setOnEditorActionListener false
        }

        addPhotoDescriptionButton.setOnClickListener {
            submitForm()
        }
    }

    private fun submitForm() {
        presenter.onClickAddDescription(et_title.text.toString())
        et_title.text.clear()
        closeKeyboard()
    }

    private fun initializeListView() {
        adapter = PhotoDescriptionViewAdapter { view, position -> rowTapped(position) }
        photoDescriptionRecyclerView.adapter = adapter
    }

    private fun rowTapped(position: Int) {
        println("🍄")
        println(adapter.photoDescriptions.value[position])
    }

    private fun closeKeyboard() {
        // this will give us the view
        // which is currently focus
        // in this layout
        val view: View? = this.currentFocus

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            val manager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}