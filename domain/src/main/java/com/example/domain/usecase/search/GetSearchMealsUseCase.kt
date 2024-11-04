package com.example.domain.usecase.search

import com.example.domain.repo.search.SearchRepo

class GetSearchMealsUseCase (private val searchRepo: SearchRepo) {
    suspend operator fun invoke(searchText: String) = searchRepo.searchMealsRequest(searchText)

}
