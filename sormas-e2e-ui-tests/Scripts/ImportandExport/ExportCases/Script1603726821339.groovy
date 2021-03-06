import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.hzi.FileHandler
import com.hzi.TestDataConnector as TestDataConnector
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

WebUI.callTestCase(findTestCase('Login/partials/LoginAsSurveillanceSupervisor'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('SurveillanceSupervisor/partials/SwitchToCases'), [:], FailureHandling.STOP_ON_FAILURE)

String firstName = TestDataConnector.getValueByKey('GenericUsers', 'first_name_case')

String lastName = TestDataConnector.getValueByKey('GenericUsers', 'last_name_case')

WebUI.setText(findTestObject('Contacts/ContactsOverview/NewContact/input_More_nameUuidEpidNumberLike'), findTestData(GlobalVariable.gContactTestDataName).getValue(2, 8))

WebUI.click(findTestObject('Contacts/ContactsOverview/div_Apply filters'))

// Wait one second for ui update
WebUI.delay(1)

WebUI.click(findTestObject('Surveillance/CaseView/div_Export'))

WebUI.click(findTestObject('Surveillance/CaseView/div_Detailed Export'))

WebUI.delay(2)

// CHECK
String dateString = new Date().format("yyyy-MM-dd")
String filename = "sormas_cases_" + dateString + ".csv"
println("file path should be: " + GlobalVariable.gDownloadPath + filename)
if (!FileHandler.existFile(GlobalVariable.gDownloadPath, filename)) {
	WebUI.closeBrowser()
	throw new StepFailedException("File '" + GlobalVariable.gDownloadPath + filename + "' was not downloaded or found")
}

WebUI.closeBrowser()