package kr.co.hdtel.facepass.newData;
import android.util.Log;
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
import kr.co.hdtel.facepass.databinding.ListDataBinding;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.NewViewHolder>{
    private List<FaceItem> faceItems;
    LinearLayout linearLayout;
    LinearLayout grLinearLayout;
    ImageButton close;
    TextView clicked;
    TextView all;
    ArrayList<CheckBox> mCheckBoxes = new ArrayList<>();
    int selectedPosition = -1;

    public NewAdapter(ArrayList<FaceItem> faceItems, LinearLayout linearLayout, LinearLayout grLinearLayout,
                      ImageButton close, TextView clicked, TextView all) {
        this.faceItems = faceItems;
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
    public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListDataBinding binding = ListDataBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NewViewHolder(binding, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder holder, int position) {
        final int pos = position; //전체 포지션
        FaceItem item = faceItems.get(pos);
        holder.bind(item);
        holder.checkBox.setTag(item);
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
        return faceItems.size();
    }

    void setItem(List<FaceItem> items) {
        if(items != null) {
            this.faceItems = items;
            notifyDataSetChanged();
        }
    }

    @BindingAdapter("bind:item")
    public static void bindItem(RecyclerView recyclerView, ArrayList<FaceItem> items) {
        NewAdapter adapter = (NewAdapter) recyclerView.getAdapter();
        if(adapter != null) {
            adapter.setItem(items);
        }
    }

    class NewViewHolder extends RecyclerView.ViewHolder{
        public CheckBox checkBox;
        ListDataBinding binding;

        public NewViewHolder(ListDataBinding binding, ViewGroup parent) {
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
        void bind(FaceItem item) {
            binding.setVariable(BR.faceList, item);
        }
    }
}
