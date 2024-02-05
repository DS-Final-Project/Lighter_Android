package com.example.test_android2.info.data

import android.os.Parcel
import android.os.Parcelable

data class SolutionDetail(
    val solutionTitle1: String?,
    val solutionContent1: String?,
    val solutionTitle2: String?,
    val solutionContent2: String?,
    val solutionTitle3: String?,
    val solutionContent3: String?,
    val solutionTitle4: String?,
    val solutionContent4: String?,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(solutionTitle1)
        parcel.writeString(solutionContent1)
        parcel.writeString(solutionTitle2)
        parcel.writeString(solutionContent2)
        parcel.writeString(solutionTitle3)
        parcel.writeString(solutionContent3)
        parcel.writeString(solutionTitle4)
        parcel.writeString(solutionContent4)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Solution> {
        override fun createFromParcel(parcel: Parcel): Solution {
            return Solution(parcel)
        }

        override fun newArray(size: Int): Array<Solution?> {
            return arrayOfNulls(size)
        }
    }

}
