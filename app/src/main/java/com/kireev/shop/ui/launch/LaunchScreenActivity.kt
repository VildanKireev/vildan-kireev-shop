package com.kireev.shop.ui.launch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kireev.shop.App
import com.kireev.shop.R
import com.kireev.shop.domain.model.remote.RemoteCategory
import com.kireev.shop.presenter.launch.LaunchPresenter
import com.kireev.shop.presenter.launch.LaunchView
import com.kireev.shop.ui.categories.CategoriesActivity
import com.kireev.shop.ui.categories.CategoriesActivity.Companion.CATEGORIES_LIST_TAG
import kotlinx.android.synthetic.main.activity_launch_screen.*
import moxy.ktx.moxyPresenter
import moxy.MvpAppCompatActivity
import javax.inject.Inject

class LaunchScreenActivity : MvpAppCompatActivity(),
    LaunchView {

    @Inject
    lateinit var launchPresenter: LaunchPresenter

    private val presenter by moxyPresenter { launchPresenter }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_screen)
    }

    override fun showMessage(stringResId: Int) {
        Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show()
    }

    override fun startCategoriesActivity(categories: List<RemoteCategory>) {
        val intent = Intent(this, CategoriesActivity::class.java)
        intent.putParcelableArrayListExtra(CATEGORIES_LIST_TAG, ArrayList(categories))
        startActivity(intent)
        splashLoadingBar.visibility = View.GONE
        finish()
    }
}
