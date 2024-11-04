package com.example.orangelastsession.ui.extensions

import android.graphics.PointF
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

//private const val CURVE_CIRCLE_RADIUS = 96
//
//// the coordinates of the first curve
//private val mFirstCurveStartPoint = PointF()
//private val mFirstCurveControlPoint1 = PointF()
//private val mFirstCurveControlPoint2 = PointF()
//private val mFirstCurveEndPoint = PointF()
//
//
//private val mSecondCurveControlPoint1 = PointF()
//private val mSecondCurveControlPoint2 = PointF()
//private var mSecondCurveStartPoint = PointF()
//private var mSecondCurveEndPoint = PointF()

class BarShape(
    private val offset: Float,
    private val circleRadius: Dp,
    private val cornerRadius: Dp,
    private val circleGap: Dp = 5.dp,
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(getPath(size, density))
    }

    private fun getPath(size: Size, density: Density): Path {
        val cutoutCenterX = offset
        val cutoutRadius = density.run { (circleRadius + circleGap).toPx() }
        val cornerRadiusPx = density.run { cornerRadius.toPx() }
        val cornerDiameter = cornerRadiusPx * 2
        return Path().apply {
            val cutoutEdgeOffset = cutoutRadius * 1.5f
            val cutoutLeftX = cutoutCenterX - cutoutEdgeOffset
            val cutoutRightX = cutoutCenterX + cutoutEdgeOffset

            // bottom left
            moveTo(x = 0F, y = size.height)
            // top left
            if (cutoutLeftX > 0) {
                val realLeftCornerDiameter = if (cutoutLeftX >= cornerRadiusPx) {
                    // there is a space between rounded corner and cutout
                    cornerDiameter
                } else {
                    // rounded corner and cutout overlap
                    cutoutLeftX * 2
                }
                arcTo(
                    rect = Rect(
                        left = 0f,
                        top = 0f,
                        right = realLeftCornerDiameter,
                        bottom = realLeftCornerDiameter
                    ),
                    startAngleDegrees = 180.0f,
                    sweepAngleDegrees = 90.0f,
                    forceMoveTo = false
                )
            }
            lineTo(cutoutLeftX, 0f)
            // cutout
            cubicTo(
                x1 = cutoutCenterX - cutoutRadius,
                y1 = 0f,
                x2 = cutoutCenterX - cutoutRadius,
                y2 = cutoutRadius,
                x3 = cutoutCenterX,
                y3 = cutoutRadius,
            )
            cubicTo(
                x1 = cutoutCenterX + cutoutRadius,
                y1 = cutoutRadius,
                x2 = cutoutCenterX + cutoutRadius,
                y2 = 0f,
                x3 = cutoutRightX,
                y3 = 0f,
            )
            // top right
            if (cutoutRightX < size.width) {
                val realRightCornerDiameter = if (cutoutRightX <= size.width - cornerRadiusPx) {
                    cornerDiameter
                } else {
                    (size.width - cutoutRightX) * 2
                }
                arcTo(
                    rect = Rect(
                        left = size.width - realRightCornerDiameter,
                        top = 0f,
                        right = size.width,
                        bottom = realRightCornerDiameter
                    ),
                    startAngleDegrees = -90.0f,
                    sweepAngleDegrees = 90.0f,
                    forceMoveTo = false
                )
            }
            // bottom right
            lineTo(x = size.width, y = size.height)
            close()
        }
    }
}

//class BottomNavCurve(private val selectedTabIndex: Int, private val tabCount: Int) : Shape {
//
//    override fun createOutline(
//        size: Size,
//        layoutDirection: LayoutDirection,
//        density: Density
//    ): Outline {
//        return Outline.Generic(path = Path().apply {
//            val curveDepth = CURVE_CIRCLE_RADIUS * -1.25f
//
//            // Define start and end points for the curves
//            val startX = (size.width / 2) - (CURVE_CIRCLE_RADIUS * 2)
//            val endX = (size.width / 2) + (CURVE_CIRCLE_RADIUS * 2)
//            val bottomY = curveDepth
//
//            // Move to the first point before the curve
//            moveTo(0f, bottomY)
//            lineTo(startX, bottomY)
//
//            // First curve
//            cubicTo(
//                startX + curveDepth, bottomY,   // Control point 1
//                startX + curveDepth, 0f,        // Control point 2
//                size.width / 2, 0f               // End point
//            )
//
//            // Second curve
//            cubicTo(
//                size.width / 2 + curveDepth, 0f,  // Control point 1
//                endX - curveDepth, bottomY,        // Control point 2
//                endX, bottomY                       // End point
//            )
//
//            // Complete the shape
//            lineTo(size.width, bottomY)
//            lineTo(size.width, size.height)
//            lineTo(0f, size.height)
//            close()
//            val curveDepth = CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 4F)
//            // the coordinates (x,y) of the start point before curve
//            mFirstCurveStartPoint.set(
//                (size.width / 2) - (CURVE_CIRCLE_RADIUS * 2) - (CURVE_CIRCLE_RADIUS / 3),
//                curveDepth
//            )
//
//            // the coordinates (x,y) of the end point after curve
//            mFirstCurveEndPoint.set(
//                size.width / 2,
//                0F
//            )
//
//            // same thing for the second curve
//            mSecondCurveStartPoint = mFirstCurveEndPoint;
//            mSecondCurveEndPoint.set(
//                (size.width / 2) + (CURVE_CIRCLE_RADIUS * 2) + (CURVE_CIRCLE_RADIUS / 3),
//                curveDepth
//            )
//
//            // the coordinates (x,y)  of the 1st control point on a cubic curve
//            mFirstCurveControlPoint1.set(
//                mFirstCurveStartPoint.x + curveDepth,
//                mFirstCurveStartPoint.y
//            )
//
//            // the coordinates (x,y)  of the 2nd control point on a cubic curve
//            mFirstCurveControlPoint2.set(
//                mFirstCurveEndPoint.x - (CURVE_CIRCLE_RADIUS * 2) + CURVE_CIRCLE_RADIUS,
//                mFirstCurveEndPoint.y
//            )
//            mSecondCurveControlPoint1.set(
//                mSecondCurveStartPoint.x + (CURVE_CIRCLE_RADIUS * 2) - CURVE_CIRCLE_RADIUS,
//                mSecondCurveStartPoint.y
//            )
//            mSecondCurveControlPoint2.set(
//                mSecondCurveEndPoint.x - (curveDepth),
//                mSecondCurveEndPoint.y
//            )
//
//            moveTo(0f, curveDepth)
//            lineTo(mFirstCurveStartPoint.x, mFirstCurveStartPoint.y)
//            cubicTo(
//                mFirstCurveControlPoint1.x, mFirstCurveControlPoint1.y,
//                mFirstCurveControlPoint2.x, mFirstCurveControlPoint2.y,
//                mFirstCurveEndPoint.x, mFirstCurveEndPoint.y
//            )
//            cubicTo(
//                mSecondCurveControlPoint1.x, mSecondCurveControlPoint1.y,
//                mSecondCurveControlPoint2.x, mSecondCurveControlPoint2.y,
//                mSecondCurveEndPoint.x, mSecondCurveEndPoint.y
//            )
//            lineTo(size.width, curveDepth)
//            lineTo(size.width, size.height)
//            lineTo(0F, size.height)
//            close()
//        })
//    }
//}