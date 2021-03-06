package org.wordpress.android.util.wizard

import android.arch.lifecycle.LiveData
import org.wordpress.android.viewmodel.SingleLiveEvent

class WizardManager<T : WizardStep>(
    private val steps: List<T>,
    private var currentStepIndex: Int = -1
) {
    val stepsCount = steps.size
    val currentStep: Int
        get() = currentStepIndex

    private val _navigatorLiveData = SingleLiveEvent<T>()
    val navigatorLiveData: LiveData<T> = _navigatorLiveData

    fun showNextStep() {
        if (isIndexValid(++currentStepIndex)) {
            _navigatorLiveData.value = steps[currentStepIndex]
        } else {
            throw IllegalStateException("Invalid index.")
        }
    }

    fun onBackPressed() {
        --currentStepIndex
    }

    private fun isIndexValid(currentStepIndex: Int): Boolean {
        return currentStepIndex >= 0 && currentStepIndex < steps.size
    }

    fun isLastStep(): Boolean {
        return !isIndexValid(currentStepIndex + 1)
    }

    fun stepPosition(T: WizardStep): Int {
        return if (steps.contains(T)) {
            steps.indexOf(T) + 1
        } else {
            throw IllegalStateException("Step $T is not present.")
        }
    }
}

/**
 * Marker interface representing a single step/screen in a wizard
 */
interface WizardStep

/**
 * Marker interface representing a state which contains all gathered data from the user input.
 */
interface WizardState

/**
 * Navigation target containing all the data needed for navigating the user to a next screen of the wizard.
 */
class WizardNavigationTarget<S : WizardStep, T : WizardState>(val wizardStep: S, val wizardState: T)
