package com.example.test_android2.data

import android.os.Parcel
import android.os.Parcelable

class Solution (
    var solutionId: Int?,
    var relation: String?,
    var keyword: String?,
    var solutionTitle: String?,
    var solutionContent: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(solutionId)
        parcel.writeString(relation)
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