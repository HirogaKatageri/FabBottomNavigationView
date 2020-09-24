package com.hirogakatageri.fabom

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.hirogakatageri.fabom.R.styleable.*

@SuppressLint("RestrictedApi")
class FabBottomNavigationView : BottomNavigationView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val fabomAttrs = context.theme.obtainStyledAttributes(
            attrs,
            FabBottomNavigationView,
            0,
            0
        )

        fabSize = fabomAttrs.getDimension(FabBottomNavigationView_fab_size, 0F)
        fabMargin = fabomAttrs.getDimension(FabBottomNavigationView_fab_margin, 0F)
        fabCornerRadius = fabomAttrs.getDimension(FabBottomNavigationView_fab_corner_radius, 0F)
        fabVerticalOffset = fabomAttrs.getDimension(FabBottomNavigationView_fab_corner_radius, 0F)

        topCurvedEdgeTreatment = BottomAppBarTopEdgeTreatment(fabMargin, fabCornerRadius, fabVerticalOffset).apply {
            fabDiameter = fabSize
        }

        val shapeAppearanceModel = ShapeAppearanceModel.Builder()
            .setTopEdge(topCurvedEdgeTreatment)
            .build()

        val primaryColorValue = TypedValue()
        context.theme?.resolveAttribute(R.attr.colorPrimary, primaryColorValue, true)

        val shapeColor: Int = fabomAttrs.getColor(FabBottomNavigationView_shapeColor, primaryColorValue.data)

        materialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            setTint(shapeColor)
            paintStyle = Paint.Style.FILL_AND_STROKE
        }

        background = materialShapeDrawable
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val fabomAttrs = context.theme.obtainStyledAttributes(
            attrs,
            FabBottomNavigationView,
            defStyleAttr,
            0
        )

        fabSize = fabomAttrs.getDimension(FabBottomNavigationView_fab_size, 0F)
        fabMargin = fabomAttrs.getDimension(FabBottomNavigationView_fab_margin, 0F)
        fabCornerRadius = fabomAttrs.getDimension(FabBottomNavigationView_fab_corner_radius, 0F)
        fabVerticalOffset = fabomAttrs.getDimension(FabBottomNavigationView_fab_corner_radius, 0F)

        topCurvedEdgeTreatment = BottomAppBarTopEdgeTreatment(
            fabMargin,
            fabCornerRadius,
            fabVerticalOffset
        ).apply { fabDiameter = fabSize }

        val shapeAppearanceModel = ShapeAppearanceModel.Builder()
            .setTopEdge(topCurvedEdgeTreatment)
            .build()

        val primaryColorValue = TypedValue()
        context.theme?.resolveAttribute(R.attr.colorPrimary, primaryColorValue, true)

        val shapeColor: Int = fabomAttrs.getColor(FabBottomNavigationView_shapeColor, primaryColorValue.data)

        materialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            setTint(shapeColor)
            paintStyle = Paint.Style.FILL_AND_STROKE
        }

        background = materialShapeDrawable
    }

    private lateinit var topCurvedEdgeTreatment: BottomAppBarTopEdgeTreatment
    private lateinit var materialShapeDrawable: MaterialShapeDrawable
    private var fabSize = 0F

    var fabMargin = 0F
    var fabCornerRadius = 0F
    var fabVerticalOffset = 0F

    fun transform(fab: FloatingActionButton) {
        if (fab.isVisible) {
            fab.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                override fun onHidden(fab: FloatingActionButton?) {
                    super.onHidden(fab)
                    ValueAnimator.ofFloat(materialShapeDrawable.interpolation, 0F).apply {
                        addUpdateListener {
                            materialShapeDrawable.interpolation = it.animatedValue as Float
                        }
                        start()
                    }
                }
            })
        } else {
            ValueAnimator.ofFloat(materialShapeDrawable.interpolation, 1F).apply {
                addUpdateListener {
                    materialShapeDrawable.interpolation = it.animatedValue as Float
                }
                doOnEnd { fab.show() }
                start()
            }
        }
    }
}
