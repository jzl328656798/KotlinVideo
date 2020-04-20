package yue.shen.kotlin.video.sc

import android.content.Context
import android.view.TextureView
import android.view.View

class ScTextureView(context: Context?) : TextureView(context) {

    private var videoHeight = 0
    private var videoWidth = 0

    fun adaptVideoSize(videoWidth: Int, videoHeight: Int) {
        if (this.videoWidth != videoWidth && this.videoHeight != videoHeight) {
            this.videoWidth = videoWidth
            this.videoHeight = videoHeight
            requestLayout()
        }
    }

    override fun setRotation(rotation: Float) {
        if (rotation != getRotation()) {
            super.setRotation(rotation)
            requestLayout()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var mWidthMeasureSpec = widthMeasureSpec
        var mHeightMeasureSpec = heightMeasureSpec
        if (rotation == 90f || rotation == 270f){
            mWidthMeasureSpec = heightMeasureSpec
            mHeightMeasureSpec = widthMeasureSpec
        }

        var width = View.getDefaultSize(videoWidth, mWidthMeasureSpec)
        var height = View.getDefaultSize(videoHeight, mHeightMeasureSpec)


        if (videoWidth > 0 && videoHeight > 0) {
            val widthSpecMode = MeasureSpec.getMode(mWidthMeasureSpec)
            val widthSpecSize = MeasureSpec.getSize(mWidthMeasureSpec)
            val heightSpecMode = MeasureSpec.getMode(mHeightMeasureSpec)
            val heightSpecSize = MeasureSpec.getSize(mHeightMeasureSpec)
            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                width = widthSpecSize
                height = heightSpecSize
                if (videoWidth * height < width * videoHeight) {
                    width = height * videoWidth / videoHeight
                } else if (videoWidth * height > width * videoHeight) {
                    height = width * videoHeight / videoWidth
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                width = widthSpecSize
                height = width * videoHeight / videoWidth
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    height = heightSpecSize
                    width = height * videoWidth / videoHeight
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                height = heightSpecSize
                width = height * videoWidth / videoHeight
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    width = widthSpecSize
                    height = width * videoHeight / videoWidth
                }
            } else {
                width = videoWidth
                height = videoHeight
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    height = heightSpecSize
                    width = height * videoWidth / videoHeight
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    width = widthSpecSize
                    height = width * videoHeight / videoWidth
                }
            }
        }
        setMeasuredDimension(width, height)
    }

}