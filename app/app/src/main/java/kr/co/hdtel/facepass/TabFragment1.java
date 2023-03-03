package kr.co.hdtel.facepass;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kr.co.hdtel.facepass.databinding.TabFragment1Binding;
import kr.co.hdtel.facepass.newData.FaceItem;
import kr.co.hdtel.facepass.newData.NewAdapter;
import kr.co.hdtel.facepass.newData.viewmodel.FaceListViewModel;
import kr.co.hdtel.server.linux.Device;
import kr.co.hdtel.server.linux.face.FaceRecognition;

public class TabFragment1 extends Fragment {
    private TabFragment1Binding binding;
    private ArrayList<FaceItem> mFaceItems;
    private NewAdapter mNewAdapter;
    private FaceListViewModel mFaceListViewModel;
    LinearLayout linearLayout;
    LinearLayout grLinearLayout;
    ImageButton close;
    TextView clicked;
    TextView all;
    CheckBox checkBox;
    int pos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.tab_fragment1, container, false);
        linearLayout = binding.linearLayout;
        grLinearLayout = binding.greenLinearLayout;
        close = binding.close;
        clicked = binding.clickedText;
        all = binding.allText;
        mFaceItems = new ArrayList<>();
        mFaceListViewModel = new ViewModelProvider(requireActivity()).get(FaceListViewModel.class);
        Device.get(requireContext()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Device>() {
            @Override
            public void accept(Device device) throws Exception {
                mFaceListViewModel.getFace(requireContext(), FaceRecognition.Type.MEMBER, device.getBuilding(), device.getRoom());
            }
        });

        mNewAdapter = new NewAdapter(mFaceItems, linearLayout, grLinearLayout, close, clicked, all);
        mNewAdapter.setOnItemClickListener(new NewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                checkBox = (CheckBox) v;
                pos = position;
                binding.used.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(view.getId() == R.id.used) {
                            mFaceItems.get(pos).setApproveYN("사용");
                            mFaceListViewModel.updateFace(requireContext(), mFaceItems.get(pos).getFaceId(), mFaceItems.get(pos).getRequestId(), true);
                            linearLayout.setVisibility(View.VISIBLE);
                            grLinearLayout.setVisibility(View.GONE);
                        }
                        mNewAdapter.notifyItemChanged(pos);
                    }
                });
                binding.unused.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(view.getId() == R.id.unused) {
                            mFaceItems.get(pos).setApproveYN("사용 안함");
                            mFaceListViewModel.updateFace(requireContext(), mFaceItems.get(pos).getFaceId(), mFaceItems.get(pos).getRequestId(), false);
                            linearLayout.setVisibility(View.VISIBLE);
                            grLinearLayout.setVisibility(View.GONE);
                        }
                        mNewAdapter.notifyItemChanged(pos);
                    }
                });
                binding.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(view.getId() == R.id.delete) {
                            mFaceListViewModel.deleteFace(requireContext(), mFaceItems.get(pos).getFaceId());
                            mFaceItems.remove(pos);
                            checkBox.setChecked(false);
                            linearLayout.setVisibility(View.VISIBLE);
                            grLinearLayout.setVisibility(View.GONE);
                        }
                        mNewAdapter.notifyItemChanged(pos);
                    }
                });
            }
        });
        binding.recyclerView.setAdapter(mNewAdapter);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFaceListViewModel.getFaceItems().observe(getViewLifecycleOwner(), new Observer<List<FaceItem>>() {
            @Override
            public void onChanged(List<FaceItem> faceItems) {
                mFaceItems = (ArrayList<FaceItem>) faceItems;
                binding.setFaceList(mFaceItems);
                mNewAdapter.notifyDataSetChanged();
            }
        });

    }
}