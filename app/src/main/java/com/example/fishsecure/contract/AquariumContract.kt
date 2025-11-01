package com.example.fishsecure.contract

import com.example.fishsecure.AquariumAdapter
import com.example.fishsecure.model.Aquarium

interface AquariumContract {

    interface View {
        fun showAquariums(aquariums: List<Aquarium>)
        fun showLoading()
        fun hideLoading()
        fun showErrorMessage(message: String)
        fun navigateTo(destination: Class<*>)
        fun navigateToDetail(destination: Class<*>, aquariumId: Int)
        fun showToast(message: String)
        fun showConfirmDeleteDialog(aquarium: Aquarium)
        fun showEditDialog(aquarium: Aquarium)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadAquariums()
        fun addNewAquarium(name: String, description: String, status: String, comment: String)
        fun deleteAquarium(aquarium: Aquarium)
        fun updateAquarium(aquarium: Aquarium)
        fun handleNavigation(itemId: Int)
        fun handleAction(aquarium: Aquarium, action: AquariumAdapter.ActionType)
    }
}