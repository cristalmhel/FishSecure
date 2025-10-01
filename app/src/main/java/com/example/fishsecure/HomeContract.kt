package com.example.fishsecure

import com.example.fishsecure.model.Aquarium

interface HomeContract {
    interface View {
        fun showAquariums(aquariums: List<Aquarium>)
    }

    interface Presenter {
        fun addAquarium()
        fun deleteAquarium(id: Int)
        fun renameAquarium(id: Int, newName: String)
    }
}
