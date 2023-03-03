package kr.co.hdtel.facepass.newData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.co.hdtel.facepass.BR;
import kr.co.hdtel.facepass.R;
import kr.co.hdtel.facepass.databinding.ListData2Binding;

public class NewAdapter2 extends RecyclerView.Adapter<NewAdapter2.NewViewHolder2> {
    private List<FaceItem2> faceItems2;
    LinearLayout linearLayout;
    LinearLayout grLinearLayout;
    ImageButton close;
    TextView clicked;
    TextView all;
    ArrayList<CheckBox> mCheckBoxes = new ArrayList<>();
    int selectedPosition = -1;

    public NewAdapter2 (ArrayList<FaceItem2> faceItems2, LinearLayout linearLayout, LinearLayout grLinearLayout,
                        ImageButton close, TextView clicked, TextView all) {
        this.faceItems2 = faceItems2;
        this.linearLayout = linearLayout;
        this.grLinearLayout = grLinearLayout;
        this.close = close;
        this.clicked = clicked;
        this.all = all;
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    @NonNull
    @Override
    public NewViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListData2Binding binding = ListData2Binding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NewViewHolder2(binding, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder2 holder, int position) {
        final int pos = position; //전체 포지션
        FaceItem2 item2 = faceItems2.get(pos);
        holder.bind(item2);
        holder.checkBox.setTag(item2);
        mCheckBoxes.add(holder.checkBox);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox) view).isChecked())
                {
                    linearLayout.setVisibility(View.GONE);
                    grLinearLayout.setVisibility(View.VISIBLE);
                    String allCnt = String.valueOf(getItemCount());
                    all.setText(allCnt);
                    clicked.setText("1");
                    for(int i = 0; i < mCheckBoxes.size(); i++) {
                        if(mCheckBoxes.get(i) == view){
                            selectedPosition = holder.getAdapterPosition();
                            mListener.onItemClick(view, selectedPosition);
                        }
                        else
                            mCheckBoxes.get(i).setChecked(false);
                    }
                }
                else {
                    selectedPosition = -1;
                    linearLayout.setVisibility(View.VISIBLE);
                    grLinearLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return faceItems2.size();
    }

    void setItem(List<FaceItem2> items2) {
        if(items2 != null) {
            this.faceItems2 = items2;
            notifyDataSetChanged();
        }
    }

    @BindingAdapter("bind:item2")
    public static void bindItem(RecyclerView recyclerView, ArrayList<FaceItem2> items2) {
        NewAdapter2 adapter = (NewAdapter2) recyclerView.getAdapter();
        if(adapter != null) {
            adapter.setItem(items2);
        }
    }
    class NewViewHolder2 extends RecyclerView.ViewHolder{
        public CheckBox checkBox;
        ListData2Binding binding;

        public NewViewHolder2(ListData2Binding binding, ViewGroup parent) {
            super(binding.getRoot());
            checkBox = binding.getRoot().findViewById(R.id.checkbox);
            this.binding = binding;

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.getId() == R.id.close) {
                        if(mCheckBoxes.size() > 0) {
                            linearLayout.setVisibility(View.VISIBLE);
                            grLinearLayout.setVisibility(View.GONE);
                            for(int i = 0; i < mCheckBoxes.size(); i++) {
                                mCheckBoxes.get(i).setChecked(false);
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }

        void bind(FaceItem2 item2) {
            binding.setVariable(BR.faceList, item2);
        }
    }
}
