package com.example.test_android2.data

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

class Chat(
    var resultNum: Int?, var doubtText1: String?, var doubtText2: String?
    , var doubtText3: String?, var doubtText4: String?, var doubtText5: String?
    , var avoidScore: Float?, var anxietyScore: Float?, var testType: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(resultNum)
        parcel.writeString(doubtText1)
        parcel.writeString(doubtText2)
        parcel.writeString(doubtText3)
        parcel.writeString(doubtText4)
        parcel.writeString(doubtText5)
        parcel.writeValue(avoidScore)
        parcel.writeValue(anxietyScore)
        parcel.writeValue(testType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<Chat> {
        override fun createFromParcel(parcel: Parcel): Chat {
            return Chat(parcel)
        }

        override fun newArray(size: Int): Array<Chat?> {
            return arrayOfNulls(size)
        }
    }

}