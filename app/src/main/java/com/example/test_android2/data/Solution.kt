package com.example.test_android2.data

import android.os.Parcel
import android.os.Parcelable

class Solution (
    var solutionId: String?,
    var relation: Int?,
    var keyword: String?,
    var solutionTitle: String?,
    var solutionContent: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(solutionId)
        relation?.let { parcel.writeInt(it) }
        parcel.writeString(keyword)
        parcel.writeString(solutionTitle)
        parcel.writeString(solutionContent)
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