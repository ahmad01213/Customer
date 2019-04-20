package com.UV.rokonalzorobyan.UV.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class branshsModle1 {
       @SerializedName("data")
       @Expose
       private List<branchModel2> data = null;
       @SerializedName("status")
       @Expose
       private Boolean status;
       @SerializedName("error")
       @Expose
       private Object error;

       public List<branchModel2> getData() {
           return data;
       }

       public void setData(List<branchModel2> data) {
           this.data = data;
       }

       public Boolean getStatus() {
           return status;
       }

       public void setStatus(Boolean status) {
           this.status = status;
       }

       public Object getError() {
           return error;
       }

       public void setError(Object error) {
           this.error = error;
       }

   }