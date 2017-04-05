package de.symeda.sormas.ui.caze;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.region.CommunityReferenceDto;
import de.symeda.sormas.api.region.DistrictReferenceDto;
import de.symeda.sormas.api.region.RegionReferenceDto;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.LayoutUtil;

@SuppressWarnings("serial")
public class CaseCreateForm extends AbstractEditForm<CaseDataDto> {
	
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";

    private static final String HTML_LAYOUT = 
			LayoutUtil.divCss(CssStyles.VSPACE2,
					LayoutUtil.fluidRowLocs(CaseDataDto.DISEASE, ""),
					LayoutUtil.fluidRowLocs(FIRST_NAME, LAST_NAME),
					LayoutUtil.fluidRowLocs(CaseDataDto.REGION, CaseDataDto.DISTRICT),
					LayoutUtil.fluidRowLocs(CaseDataDto.COMMUNITY, CaseDataDto.HEALTH_FACILITY)
					);

    public CaseCreateForm() {
        super(CaseDataDto.class, CaseDataDto.I18N_PREFIX);

        setWidth(540, Unit.PIXELS);
    }

    @Override
	protected void addFields() {

    	addField(CaseDataDto.DISEASE, NativeSelect.class);
    	
    	addCustomField(FIRST_NAME, String.class, TextField.class);
    	addCustomField(LAST_NAME, String.class, TextField.class);

    	ComboBox region = addField(CaseDataDto.REGION, ComboBox.class);
    	ComboBox district = addField(CaseDataDto.DISTRICT, ComboBox.class);
    	ComboBox community = addField(CaseDataDto.COMMUNITY, ComboBox.class);
    	ComboBox facility = addField(CaseDataDto.HEALTH_FACILITY, ComboBox.class);
    	
    	region.addValueChangeListener(e -> {
    		district.removeAllItems();
    		RegionReferenceDto regionDto = (RegionReferenceDto)e.getProperty().getValue();
    		if (regionDto != null) {
    			district.addItems(FacadeProvider.getDistrictFacade().getAllByRegion(regionDto.getUuid()));
    		}
       	});
    	district.addValueChangeListener(e -> {
    		community.removeAllItems();
    		DistrictReferenceDto districtDto = (DistrictReferenceDto)e.getProperty().getValue();
    		if (districtDto != null) {
    			community.addItems(FacadeProvider.getCommunityFacade().getAllByDistrict(districtDto.getUuid()));
    		}
    	});
    	community.addValueChangeListener(e -> {
    		facility.removeAllItems();
    		CommunityReferenceDto communityDto = (CommunityReferenceDto)e.getProperty().getValue();
    		if (communityDto != null) {
    			facility.addItems(FacadeProvider.getFacilityFacade().getAllByCommunity(communityDto));
    		}
    	});
		region.addItems(FacadeProvider.getRegionFacade().getAllAsReference());

    	setRequired(true, FIRST_NAME, LAST_NAME, CaseDataDto.DISEASE, 
    			CaseDataDto.REGION, CaseDataDto.DISTRICT, CaseDataDto.COMMUNITY, CaseDataDto.HEALTH_FACILITY);
    }
    
    public String getPersonFirstName() {
    	return (String)getField(FIRST_NAME).getValue();
    }

    public String getPersonLastName() {
    	return (String)getField(LAST_NAME).getValue();
    }
    
    public void setPerson(PersonDto person) {
    	((TextField) getField(FIRST_NAME)).setValue(person.getFirstName());
    	((TextField) getField(LAST_NAME)).setValue(person.getLastName());
    	if (person.getAddress() != null) {
    		ComboBox region = (ComboBox) getField(CaseDataDto.REGION);
    		ComboBox district = (ComboBox) getField(CaseDataDto.DISTRICT);
    		ComboBox community = (ComboBox) getField(CaseDataDto.COMMUNITY);
    		region.setValue(person.getAddress().getRegion());
    		if (region.getValue() != null) {
	    		district.removeAllItems();
	    		district.addItems(FacadeProvider.getDistrictFacade().getAllByRegion(((RegionReferenceDto) region.getValue()).getUuid()));
	    		district.setValue(person.getAddress().getDistrict());
	    		if (district.getValue() != null) {
		    		community.removeAllItems();
		    		community.addItems(FacadeProvider.getCommunityFacade().getAllByDistrict(((DistrictReferenceDto) district.getValue()).getUuid()));
		    		community.setValue(person.getAddress().getCommunity());
	    		}
    		}
    	}
    }
    
    public void setNameReadOnly(boolean readOnly) {
    	getField(FIRST_NAME).setEnabled(!readOnly);
    	getField(LAST_NAME).setEnabled(!readOnly);
    }
    
    public void setDiseaseReadOnly(boolean readOnly) {
    	getField(CaseDataDto.DISEASE).setEnabled(!readOnly);
    }
    
	@Override
	protected String createHtmlLayout() {
		 return HTML_LAYOUT;
	}
}
