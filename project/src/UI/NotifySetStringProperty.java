/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import javafx.beans.property.SimpleStringProperty;

public class NotifySetStringProperty extends SimpleStringProperty{

        private OnSetValueListener valueListener;

        public NotifySetStringProperty(String initialValue) {
            super(initialValue);
        }

        @Override
        public void set(String newValue) {
            super.set(newValue);
            if(valueListener!= null) {
                valueListener.onValueSet(newValue);
            }
        }

        public void setValueListener(OnSetValueListener valueListener) {
            this.valueListener = valueListener;
        }

        public interface OnSetValueListener {
            void onValueSet(String value);
        }
    }
