package com.aplex.aplextest.fragment.buzz;

import com.aplex.aplextest.R;
import com.aplex.aplextest.jninative.AplexBuzz;

public class BuzzPresenter extends BuzzContract.Persenter{

	@Override
	public void ChooseHZ() {
		BuzzFragment buzzFragment = (BuzzFragment)mView;
		// TODO Auto-generated method stub
        String text = "";
        switch (buzzFragment.mRadioGroup.getCheckedRadioButtonId()) {
            case R.id.buzz_hz_500:
                text = "500";
                break;
            case R.id.buzz_hz_1000:
                text = "1000";
                break;
            case R.id.buzz_hz_1500:
                text = "1500";
                break;
            case R.id.buzz_hz_2000:
                text = "2000";
                break;
            default:
                text = "500";
                break;
        }
        if (buzzFragment.mFlatToggleButton.isChecked()) {
            AplexBuzz.setFrequency(Integer.valueOf(text));    // set buzzer frequency
            AplexBuzz.enable();
        } else {
            AplexBuzz.disable();
        }
		
	}
	@Override
	public void onDestroy() {
		AplexBuzz.disable();
	}
}
