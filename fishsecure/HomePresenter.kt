package com.example.fishsecure

import com.example.fishsecure.model.Aquarium

class HomePresenter(private val view: HomeContract.View) : HomeContract.Presenter {

    private val aquariums = mutableListOf<Aquarium>()
    private var count = 0

    override fun addAquarium() {
        count++
        aquariums.add(Aquarium(count, "Aquarium #$count"))
        view.showAquariums(aquariums)
    }

    override fun deleteAquarium(id: Int) {
        aquariums.removeAll { it.id == id }
        view.showAquariums(aquariums)
    }

    override fun renameAquarium(id: Int, newName: String) {
        val aquarium = aquariums.find { it.id == id }
        if (aquarium != null) {
            aquarium.name = newName
            view.showAquariums(aquariums)
        }
    }
}
